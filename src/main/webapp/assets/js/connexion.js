// assets/js/connexion.js

document.addEventListener('DOMContentLoaded', () => {
    
    const form = document.getElementById('loginForm');
    if (!form) return;
    
    const messageDiv = document.getElementById('message');
    const btnSubmit = document.getElementById('btn-submit');
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const btnTextOriginal = btnSubmit.innerHTML;
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = '<span>Connexion en cours...</span>';
        
        try {
            const formData = new URLSearchParams(new FormData(form));
            
            // ⚠️ ADAPTE l'URL à ton contextPath
            const response = await fetch('/connexion', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            });
            
            if (!response.ok) {
                throw new Error('HTTP ' + response.status);
            }
            
            const data = await response.json();
            
            messageDiv.style.display = 'block';
            
            if (data.success) {
                messageDiv.style.background = '#d4edda';
                messageDiv.style.color = '#155724';
                messageDiv.textContent = '✅ ' + data.message;
                
                // Redirige vers le dashboard
                setTimeout(() => {
                    window.location.href = data.redirect;   // URL fournie par le Servlet
                }, 800);
            } else {
                messageDiv.style.background = '#f8d7da';
                messageDiv.style.color = '#721c24';
                messageDiv.textContent = '❌ ' + data.message;
            }
            
        } catch (err) {
            console.error(err);
            messageDiv.style.display = 'block';
            messageDiv.style.background = '#f8d7da';
            messageDiv.style.color = '#721c24';
            messageDiv.textContent = '❌ Erreur réseau, veuillez réessayer';
        } finally {
            btnSubmit.disabled = false;
            btnSubmit.innerHTML = btnTextOriginal;
        }
    });
});