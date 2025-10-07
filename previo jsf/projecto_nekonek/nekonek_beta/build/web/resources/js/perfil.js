// Initialize app when DOM is ready
document.addEventListener("DOMContentLoaded", () => {
  initializeEventListeners()
})

function initializeEventListeners() {
  // Profile upload
  const profileUpload = document.getElementById("profile-upload")
  if (profileUpload) {
    profileUpload.addEventListener("change", handleProfileImageUpload)
  }

  // Time filter buttons
  document.querySelectorAll(".time-filter-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      document.querySelectorAll(".time-filter-btn").forEach((b) => b.classList.remove("active"))
      this.classList.add("active")
    })
  })
}

// Profile functions
function handleProfileImageUpload(event) {
  const file = event.target.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      const container = document.getElementById("profileImageContainer")
      if (container) {
        container.innerHTML = `<img src="${e.target.result}" alt="Profile">`
      }
    }
    reader.readAsDataURL(file)

    showSuccessMessage("Imagen de perfil actualizada")
  }
}

function openEditProfileModal() {
  const modal = document.getElementById("editProfileModal")
  if (modal && window.bootstrap !== "undefined") {
    new window.bootstrap.Modal(modal).show()
  }
}

function closeEditProfileModal() {
  const modal = document.getElementById("editProfileModal")
  if (modal && window.bootstrap !== "undefined") {
    const modalInstance = window.bootstrap.Modal.getInstance(modal)
    if (modalInstance) {
      modalInstance.hide()
    }
  }
  showSuccessMessage("Perfil actualizado exitosamente")
}

// Stats filter function
function filterStats(period) {
  // Esta función será implementada por el bean de estadísticas
  console.log("Filtering stats for period:", period)
}

// Utility functions
function showSuccessMessage(message) {
  const notification = document.createElement("div")
  notification.className = "alert alert-success position-fixed top-0 end-0 m-3"
  notification.style.zIndex = "9999"
  notification.innerHTML = `
        <i class="fas fa-check-circle me-2"></i>
        ${message}
    `

  document.body.appendChild(notification)

  setTimeout(() => {
    notification.remove()
  }, 3000)
}

function showErrorMessage(message) {
  const notification = document.createElement("div")
  notification.className = "alert alert-danger position-fixed top-0 end-0 m-3"
  notification.style.zIndex = "9999"
  notification.innerHTML = `
        <i class="fas fa-exclamation-circle me-2"></i>
        ${message}
    `

  document.body.appendChild(notification)

  setTimeout(() => {
    notification.remove()
  }, 3000)
}
