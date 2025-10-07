document.addEventListener("DOMContentLoaded", () => {
  const mobileMenuBtn = document.getElementById("mobileMenuBtn")
  const mobileNav = document.getElementById("mobileNav")
  const menuIcon = document.getElementById("menuIcon")
  const closeIcon = document.getElementById("closeIcon")

  if (mobileMenuBtn && mobileNav) {
    mobileMenuBtn.addEventListener("click", () => {
      const isOpen = mobileNav.classList.contains("open")

      if (isOpen) {
        mobileNav.classList.remove("open")
        menuIcon.classList.remove("hidden")
        closeIcon.classList.add("hidden")
      } else {
        mobileNav.classList.add("open")
        menuIcon.classList.add("hidden")
        closeIcon.classList.remove("hidden")
      }
    })
  }

  // Cerrar menú móvil al hacer clic en un enlace
  const mobileLinks = document.querySelectorAll(".nav-mobile-link, .nav-mobile-button")
  mobileLinks.forEach((link) => {
    link.addEventListener("click", () => {
      mobileNav.classList.remove("open")
      menuIcon.classList.remove("hidden")
      closeIcon.classList.add("hidden")
    })
  })
})
