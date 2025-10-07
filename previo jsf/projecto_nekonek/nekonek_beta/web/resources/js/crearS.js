// Validación de URLs de imagen
            function validarURL(input) {
                const url = input.value.trim();
                if (url && !url.match(/^https?:\/\/.+\.(jpg|jpeg|png|gif|webp)$/i)) {
                    input.style.borderColor = '#e74c3c';
                    alert('⚠️ Por favor, ingresa una URL válida de imagen.\nFormatos soportados: JPG, JPEG, PNG, GIF, WebP');
                    input.focus();
                    return false;
                } else if (url) {
                    input.style.borderColor = '#00b894';
                }
                return true;
            }
            
            // Validación general del formulario
            function validarFormulario() {
                const titulo = document.getElementById('crearSerieForm:titulo').value.trim();
                const descripcion = document.getElementById('crearSerieForm:descripcion').value.trim();
                const episodios = document.getElementById('crearSerieForm:episodios').value.trim();
                
                if (!titulo) {
                    alert('⚠️ El título es obligatorio');
                    document.getElementById('crearSerieForm:titulo').focus();
                    return false;
                }
                
                if (!descripcion) {
                    alert('⚠️ La descripción es obligatoria');
                    document.getElementById('crearSerieForm:descripcion').focus();
                    return false;
                }
                
                if (!episodios || isNaN(episodios) || parseInt(episodios) < 1) {
                    alert('⚠️ La cantidad de episodios debe ser un número mayor a 0');
                    document.getElementById('crearSerieForm:episodios').focus();
                    return false;
                }
                
                // Mostrar mensaje de carga
                const submitBtn = event.target;
                submitBtn.disabled = true;
                submitBtn.value = '⏳ Creando serie...';
                submitBtn.style.opacity = '0.7';
                
                return true;
            }
            
            // Contador de caracteres para descripción
            document.addEventListener('DOMContentLoaded', function() {
                const descripcionTextarea = document.getElementById('crearSerieForm:descripcion');
                if (descripcionTextarea) {
                    descripcionTextarea.addEventListener('input', function() {
                        const maxLength = 500; // Puedes ajustar este límite
                        const currentLength = this.value.length;
                        
                        // Crear o actualizar contador si no existe
                        let counter = this.parentNode.querySelector('.char-counter');
                        if (!counter) {
                            counter = document.createElement('small');
                            counter.className = 'char-counter';
                            counter.style.color = '#6c757d';
                            counter.style.fontSize = '12px';
                            this.parentNode.appendChild(counter);
                        }
                        
                        counter.textContent = currentLength + ' caracteres';
                        
                        if (currentLength > maxLength * 0.9) {
                            counter.style.color = '#e74c3c';
                        } else {
                            counter.style.color = '#6c757d';
                        }
                    });
                }
            });
            
            // Auto-resize para textarea
            function autoResize(textarea) {
                textarea.style.height = 'auto';
                textarea.style.height = textarea.scrollHeight + 'px';
            }
            
            // Aplicar auto-resize a la descripción
            document.addEventListener('DOMContentLoaded', function() {
                const textarea = document.getElementById('crearSerieForm:descripcion');
                if (textarea) {
                    textarea.addEventListener('input', function() {
                        autoResize(this);
                    });
                }
            });