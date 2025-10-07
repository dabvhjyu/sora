// Carousel functionality
document.addEventListener("DOMContentLoaded", () => {
  // Variables
  const slides = document.querySelectorAll(".slide")
  const indicators = document.querySelectorAll(".indicator")
  const prevBtn = document.querySelector(".prev-btn")
  const nextBtn = document.querySelector(".next-btn")
  let currentSlide = 0
  let slideInterval

  // Functions
  function showSlide(index) {
    // Hide current slide
    slides[currentSlide].classList.remove("active")
    indicators[currentSlide].classList.remove("active")

    // Update index
    currentSlide = index

    // If index is greater than number of slides, go to first
    if (currentSlide >= slides.length) {
      currentSlide = 0
    }

    // If index is less than 0, go to last slide
    if (currentSlide < 0) {
      currentSlide = slides.length - 1
    }

    // Show new slide
    slides[currentSlide].classList.add("active")
    indicators[currentSlide].classList.add("active")
  }

  function nextSlide() {
    showSlide(currentSlide + 1)
  }

  function prevSlide() {
    showSlide(currentSlide - 1)
  }

  function startSlideshow() {
    slideInterval = setInterval(nextSlide, 10000) // 10 seconds
  }

  function stopSlideshow() {
    clearInterval(slideInterval)
  }

  // Event Listeners
  prevBtn.addEventListener("click", () => {
    prevSlide()
    stopSlideshow()
    startSlideshow()
  })

  nextBtn.addEventListener("click", () => {
    nextSlide()
    stopSlideshow()
    startSlideshow()
  })

  indicators.forEach((indicator) => {
    indicator.addEventListener("click", function () {
      const index = Number.parseInt(this.getAttribute("data-index"))
      showSlide(index)
      stopSlideshow()
      startSlideshow()
    })
  })

  // Start automatic slideshow
  startSlideshow()
})

// Rankings hover functionality
document.addEventListener("DOMContentLoaded", () => {
  const rankingItems = document.querySelectorAll(".ranking-item")

  rankingItems.forEach((item) => {
    item.addEventListener("touchstart", function () {
      const wasExpanded = this.classList.contains("expanded")

      // Close all expanded items
      rankingItems.forEach((i) => i.classList.remove("expanded"))

      // If it wasn't expanded, expand it
      if (!wasExpanded) {
        this.classList.add("expanded")
      }
    })
  })
})

// Genre tabs functionality
document.addEventListener("DOMContentLoaded", () => {
  const genreTabButtons = document.querySelectorAll(".genre-tab-button")
  const genreGrids = document.querySelectorAll(".genre-grid")

  genreTabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      // Skip if it's the "Ver más" button
      if (button.classList.contains("ver-mas")) {
        return
      }

      // Remove active class from all buttons and grids
      genreTabButtons.forEach((btn) => btn.classList.remove("active"))
      genreGrids.forEach((grid) => grid.classList.remove("active"))

      // Add active class to clicked button and corresponding grid
      button.classList.add("active")
      const tabId = button.getAttribute("data-tab")
      const targetGrid = document.getElementById(tabId)
      if (targetGrid) {
        targetGrid.classList.add("active")
      }
    })
  })
})

// Regional series tabs functionality
document.addEventListener("DOMContentLoaded", () => {
  const tabButtons = document.querySelectorAll(".tab-button")
  const seriesGrids = document.querySelectorAll(".series-grid")

  tabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      // Remove active class from all buttons and grids
      tabButtons.forEach((btn) => btn.classList.remove("active"))
      seriesGrids.forEach((grid) => grid.classList.remove("active"))

      // Add active class to clicked button and corresponding grid
      button.classList.add("active")
      const tabId = button.getAttribute("data-tab")
      const targetGrid = document.getElementById(tabId)
      if (targetGrid) {
        targetGrid.classList.add("active")
      }
    })
  })
})

