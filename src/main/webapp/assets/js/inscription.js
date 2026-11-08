// assets/js/inscription.js

document.addEventListener('DOMContentLoaded', () => {
    
    const form = document.getElementById('registerForm');
    if (!form) return;
    
    const messageDiv = document.getElementById('message');
    const btnSubmit = document.getElementById('btn-submit');
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();   // ⚠️ EMPÊCHE l'envoi classique
        
        // Désactiver le bouton pendant la requête
        const btnTextOriginal = btnSubmit.innerHTML;
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = '<span>Inscription en cours...</span>';
        
        try {
            // 1. Envoyer les données au Servlet
            const formData = new URLSearchParams(new FormData(form));
            
            const response = await fetch('/inscription', {   // ← adapte l'URL !
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            });
            
            if (!response.ok) {
                throw new Error('HTTP ' + response.status);
            }
            
            // 2. Lire la réponse JSON
            const data = await response.json();
            
            // 3. Afficher le message
            messageDiv.style.display = 'block';
            
            if (data.success) {
                messageDiv.style.background = '#d4edda';
                messageDiv.style.color = '#155724';
                messageDiv.style.border = '1px solid #c3e6cb';
                messageDiv.textContent = '✅ ' + data.message;
                
                form.reset();
                // Optionnel : rediriger après 2s
                // setTimeout(() => window.location.href = 'index.html', 2000);
            } else {
                messageDiv.style.background = '#f8d7da';
                messageDiv.style.color = '#721c24';
                messageDiv.style.border = '1px solid #f5c6cb';
                messageDiv.textContent = '❌ ' + data.message;
            }
            
        } catch (err) {
            console.error(err);
            messageDiv.style.display = 'block';
            messageDiv.style.background = '#f8d7da';
            messageDiv.style.color = '#721c24';
            messageDiv.style.border = '1px solid #f5c6cb';
            messageDiv.textContent = '❌ Erreur réseau, veuillez réessayer';
        } finally {
            // Réactiver le bouton
            btnSubmit.disabled = false;
            btnSubmit.innerHTML = btnTextOriginal;
        }
    });
});