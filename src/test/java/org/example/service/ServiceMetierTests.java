package org.example.service;

import org.example.dao.EtudiantsDAO;
import org.example.dao.NoteDAO;
import org.example.dao.UtilisateurDAO;
import org.example.modele.Bulletin;
import org.example.modele.Etudiants;
import org.example.modele.Note;
import org.example.modele.Utilisateur;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ServiceMetierTests {

    private static void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static class FakeUtilisateurDAO extends UtilisateurDAO {
        private final Map<String, Utilisateur> byEmail = new HashMap<>();
        private boolean emailExists;
        private Utilisateur created;

        @Override
        public Utilisateur creer(Utilisateur u) {
            created = u;
            u.setId(42);
            byEmail.put(u.getEmail(), u);
            return u;
        }

        @Override
        public Optional<Utilisateur> trouverParEmail(String email) {
            return Optional.ofNullable(byEmail.get(email));
        }

        @Override
        public boolean emailExiste(String email) {
            return byEmail.containsKey(email) || emailExists;
        }
    }

    private static class FakeEtudiantsDAO extends EtudiantsDAO {
        private final Map<Integer, Etudiants> byId = new HashMap<>();
        private final Map<String, Etudiants> byEmail = new HashMap<>();

        @Override
        public boolean emailExiste(String email) {
            return byEmail.containsKey(email);
        }

        @Override
        public Optional<Etudiants> trouverParId(int id) {
            return Optional.ofNullable(byId.get(id));
        }

        @Override
        public Optional<Etudiants> trouverParEmail(String email) {
            return Optional.ofNullable(byEmail.get(email));
        }

        @Override
        public boolean modifier(Etudiants e) {
            if (!byId.containsKey(e.getId())) {
                return false;
            }
            byId.put(e.getId(), e);
            byEmail.put(e.getEmail(), e);
            return true;
        }

        @Override
        public boolean supprimer(int id) {
            Etudiants removed = byId.remove(id);
            if (removed != null) {
                byEmail.remove(removed.getEmail());
                return true;
            }
            return false;
        }

        @Override
        public Etudiants ajouter(Etudiants e) {
            e.setId(100 + byId.size());
            byId.put(e.getId(), e);
            byEmail.put(e.getEmail(), e);
            return e;
        }
    }

    private static class FakeNoteDAO extends NoteDAO {
        private final Map<Integer, List<Note>> notesByEtudiant = new HashMap<>();

        @Override
        public List<Note> notesParEtudiant(int etudiantId) {
            return notesByEtudiant.getOrDefault(etudiantId, new ArrayList<>());
        }
    }

    @Test
    void connexion_should_succeed_with_valid_credentials() throws Exception {
        ConnexionService service = new ConnexionService();
        FakeUtilisateurDAO dao = new FakeUtilisateurDAO();
        injectField(service, "doa", dao);

        String hash = BCrypt.hashpw("admin123", BCrypt.gensalt(12));
        Utilisateur admin = new Utilisateur(1, "Administrateur", "admin@mail.com", hash, LocalDateTime.now());
        dao.byEmail.put("admin@mail.com", admin);

        ConnexionService.ResultatConnexion result = service.connecter("admin@mail.com", "admin123");

        assertTrue(result.succes);
        assertEquals("Connexion réussie ! Bienvenue Administrateur", result.message);
        assertNotNull(result.utilisateur);
        assertEquals("Administrateur", result.utilisateur.getNom());
    }

    @Test
    void connexion_should_fail_when_password_is_wrong() throws Exception {
        ConnexionService service = new ConnexionService();
        FakeUtilisateurDAO dao = new FakeUtilisateurDAO();
        injectField(service, "doa", dao);

        String hash = BCrypt.hashpw("admin123", BCrypt.gensalt(12));
        Utilisateur admin = new Utilisateur(1, "Administrateur", "admin@mail.com", hash, LocalDateTime.now());
        dao.byEmail.put("admin@mail.com", admin);

        ConnexionService.ResultatConnexion result = service.connecter("admin@mail.com", "mauvais-mdp");

        assertFalse(result.succes);
        assertEquals("Email ou mot de passe incorrect", result.message);
        assertNull(result.utilisateur);
    }

    @Test
    void connexion_should_fail_when_email_is_missing() {
        ConnexionService service = new ConnexionService();

        ConnexionService.ResultatConnexion result = service.connecter("", "admin123");

        assertFalse(result.succes);
        assertEquals("L'email est obligatoire", result.message);
    }

    @Test
    void inscription_should_create_user_when_inputs_are_valid() throws Exception {
        InscriptionService service = new InscriptionService();
        FakeUtilisateurDAO dao = new FakeUtilisateurDAO();
        injectField(service, "dao", dao);

        InscriptionService.ResultatInscription result = service.inscrire(
                "Alice",
                "alice@mail.com",
                "motdepasse",
                "motdepasse"
        );

        assertTrue(result.succes);
        assertNotNull(result.utilisateur);
        assertEquals("Alice", result.utilisateur.getNom());
        assertTrue(BCrypt.checkpw("motdepasse", result.utilisateur.getMotDePasseHash()));
        assertTrue(dao.byEmail.containsKey("alice@mail.com"));
    }

    @Test
    void inscription_should_reject_duplicate_email() throws Exception {
        InscriptionService service = new InscriptionService();
        FakeUtilisateurDAO dao = new FakeUtilisateurDAO();
        injectField(service, "dao", dao);
        dao.byEmail.put("alice@mail.com", new Utilisateur(1, "Alice", "alice@mail.com", BCrypt.hashpw("existing", BCrypt.gensalt(12)), LocalDateTime.now()));

        InscriptionService.ResultatInscription result = service.inscrire(
                "Alice",
                "alice@mail.com",
                "motdepasse",
                "motdepasse"
        );

        assertFalse(result.succes);
        assertEquals("Cet email est déjà utilisé", result.message);
    }

    @Test
    void inscription_should_reject_invalid_confirmation() throws Exception {
        InscriptionService service = new InscriptionService();
        FakeUtilisateurDAO dao = new FakeUtilisateurDAO();
        injectField(service, "dao", dao);

        InscriptionService.ResultatInscription result = service.inscrire(
                "Alice",
                "alice@mail.com",
                "motdepasse",
                "autremdp"
        );

        assertFalse(result.succes);
        assertEquals("Les mots de passe ne correspondent pas", result.message);
    }

    @Test
    void etudiant_should_reject_invalid_email_on_add() throws Exception {
        EtudiantService service = new EtudiantService();
        FakeEtudiantsDAO dao = new FakeEtudiantsDAO();
        injectField(service, "dao", dao);

        EtudiantService.Resultat result = service.ajouter("A", "email-invalide", "0123456789", "INFO", "L3");

        assertFalse(result.succes);
        assertEquals("L'email est invalide", result.message);
    }

    @Test
    void etudiant_should_modify_existing_student() throws Exception {
        EtudiantService service = new EtudiantService();
        FakeEtudiantsDAO dao = new FakeEtudiantsDAO();
        injectField(service, "dao", dao);

        Etudiants existing = new Etudiants(7, "Alice", "alice@mail.com", "0601020304", "INFO", "L3");
        dao.byId.put(7, existing);
        dao.byEmail.put("alice@mail.com", existing);

        EtudiantService.Resultat result = service.modifier(
                7,
                "Alice",
                "alice@nouveau.com",
                "0601020304",
                "INFO",
                "M1"
        );

        assertTrue(result.succes);
        assertEquals("Étudiant modifié avec succès", result.message);
        assertEquals("alice@nouveau.com", result.etudiant.getEmail());
    }

    @Test
    void etudiant_should_delete_existing_student() throws Exception {
        EtudiantService service = new EtudiantService();
        FakeEtudiantsDAO dao = new FakeEtudiantsDAO();
        injectField(service, "dao", dao);

        Etudiants existing = new Etudiants(9, "Bob", "bob@mail.com", "0708091011", "INFO", "L3");
        dao.byId.put(9, existing);
        dao.byEmail.put("bob@mail.com", existing);

        EtudiantService.Resultat result = service.supprimer(9);

        assertTrue(result.succes);
        assertEquals("Étudiant supprimé", result.message);
        assertEquals("Bob", result.etudiant.getNom());
    }

    @Test
    void bulletin_should_build_for_student_with_notes() throws Exception {
        BulletinService service = new BulletinService();
        FakeEtudiantsDAO etudiantDao = new FakeEtudiantsDAO();
        FakeNoteDAO noteDao = new FakeNoteDAO();
        injectField(service, "etudiantDAO", etudiantDao);
        injectField(service, "noteDAO", noteDao);

        Etudiants etudiant = new Etudiants(3, "Charlie", "charlie@mail.com", "0600000000", "INFO", "M1");
        etudiantDao.byId.put(3, etudiant);
        etudiantDao.byEmail.put("charlie@mail.com", etudiant);

        Note note1 = new Note(1, 3, 10, 18.0, LocalDate.of(2026, 10, 1));
        note1.setCoursCode("INFO101");
        note1.setCoursIntitule("Informatique");
        note1.setCoefficient(4);

        Note note2 = new Note(2, 3, 11, 12.0, LocalDate.of(2026, 10, 2));
        note2.setCoursCode("MATH101");
        note2.setCoursIntitule("Mathématiques");
        note2.setCoefficient(3);

        noteDao.notesByEtudiant.put(3, List.of(note1, note2));

        Optional<Bulletin> result = service.bulletinEtudiant(3);

        assertTrue(result.isPresent());
        assertEquals("Charlie", result.get().getEtudiantNom());
        assertEquals(2, result.get().getNotes().size());
        assertEquals(15.0, result.get().getMoyenneGenerale(), 0.001);
    }
}
