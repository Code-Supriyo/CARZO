document.addEventListener("DOMContentLoaded", function() {

    const tabs = document.querySelectorAll(".car-tab");
    const indicator = document.querySelector(".tab-indicator");
    const carGrid = document.getElementById("carGrid");
    const brandGrid = document.getElementById("brandGrid");
    const carSearchInput = document.getElementById("carSearchInput");
    const searchSuggestions = document.getElementById("searchSuggestions");
    const searchIcon = document.querySelector(".search-icon");
    let activeCategory = "popular";
    let allCars = [];
    let allBrands = [];
    let selectedBrandId = null;
	
    function assetPath(path, folder) {
        if (!path) return "";

        const normalizedPath = String(path).trim().replace(/\\/g, "/");

        if (/^(https?:)?\/\//i.test(normalizedPath) ||
            normalizedPath.startsWith("/")) {
            return normalizedPath;
        }

        if (normalizedPath.toLowerCase().startsWith(folder.toLowerCase() + "/")) {
            return normalizedPath;
        }

        return folder + "/" + normalizedPath;
    }

    function formatPrice(car) {
        const min = car.minPrice;
        const max = car.maxPrice;

        if (min == null && max == null) return "Price not available";
        if (min == null) return "Up to ₹" + Number(max).toFixed(2) + " Lakh";
        if (max == null) return "From ₹" + Number(min).toFixed(2) + " Lakh";

        return "₹" + Number(min).toFixed(2) +
            " - ₹" + Number(max).toFixed(2) + " Lakh";
    }

    function createCarCard(car) {

        const article = document.createElement("article");
        article.className = "car-card";
		article.dataset.carId = car.carId;
        const imageBox = document.createElement("div");
        imageBox.className = "car-image";

        const image = document.createElement("img");
        image.src = assetPath(car.imageUrl, "cars");
        image.alt = car.carName || "Car";

        image.onerror = function() {
            this.style.visibility = "hidden";
        };

        const wishlist = document.createElement("button");
        wishlist.type = "button";
        wishlist.className = "wishlist-button";
        wishlist.setAttribute("aria-label", "Add to wishlist");

        wishlist.innerHTML =
            '<img src="heart-svgrepo-com.svg" alt="Wishlist" class="heart-outline">' +
            '<img src="heart-fill-svgrepo-com.svg" alt="Wishlisted" class="heart-filled">';

        imageBox.append(image, wishlist);

        const details = document.createElement("div");
        details.className = "car-details";

        const name = document.createElement("h3");
        name.textContent = car.carName || "Unnamed car";

        const price = document.createElement("p");
        price.textContent = formatPrice(car);

        const compare = document.createElement("button");
        compare.type = "button";
        compare.className = "compare-button";

        compare.innerHTML =
            '<span class="compare-content">' +
            '<img src="plus-circle-svgrepo-com.svg" alt="Add" class="compare-plus">' +
            '<span class="compare-text">Add to Compare</span>' +
            '<img src="tick-circle-svgrepo-com.svg" alt="Added" class="compare-tick">' +
            '</span>';

        details.append(name, price, compare);
        article.append(imageBox, details);

        return article;
    }

    function moveIndicator(tab, animate) {

        if (!indicator || !tab) return;

        const tabsContainer = document.getElementById("carTabs");
        if (!tabsContainer) return;

        const tabRect = tab.getBoundingClientRect();
        const containerRect = tabsContainer.getBoundingClientRect();

        indicator.style.transition = animate === false
            ? "none"
            : "left 0.3s ease, width 0.3s ease";

        indicator.style.left =
            (tabRect.left - containerRect.left) + "px";

        indicator.style.width = tabRect.width + "px";

        if (animate === false) {
            requestAnimationFrame(function() {
                indicator.style.transition = "left 0.3s ease, width 0.3s ease";
            });
        }
    }
    function getCarsForCurrentView() {

        let result = allCars.slice();

        if (selectedBrandId !== null) {
            result = result.filter(function(car) {
                return Number(car.brandId) === Number(selectedBrandId);
            });
        }

        if (activeCategory === "popular") {

            result = result.filter(function(car) {
                return Number(car.isPopular) === 1;
            });

        } else if (activeCategory === "new-launches") {

            result = result.filter(function(car) {
                return String(car.launchStatus || "")
                    .toUpperCase().includes("NEW");
            });

        } else if (activeCategory === "upcoming") {

            result = result.filter(function(car) {
                return String(car.launchStatus || "")
                    .toUpperCase().includes("UPCOMING");
            });
        }
        return result;
    }

    function displayCars() {

        if (!carGrid) return;

        carGrid.innerHTML = "";

        const cars = getCarsForCurrentView();

        if (cars.length === 0) {
            carGrid.innerHTML =
                '<p class="no-cars-message">No cars available in this section.</p>';
            return;
        }

        cars.forEach(function(car) {
            carGrid.appendChild(createCarCard(car));
        });
    }

    function loadCars() {

        if (!carGrid) return;

        carGrid.innerHTML =
            '<p class="no-cars-message">Loading cars...</p>';

        const params = new URLSearchParams({
            action: "list"
        });

        fetch("CarServ?" + params.toString())
            .then(function(response) {

                if (!response.ok) {
                    throw new Error("CarServlet returned " + response.status);
                }

                return response.json();
            })
            .then(function(data) {

                if (!Array.isArray(data)) {
                    throw new Error(data.error || "Invalid car response");
                }

                allCars = data;
                displayCars();
            })
            .catch(function(error) {

                console.error("Car loading error:", error);

                carGrid.innerHTML =
                    '<p class="no-cars-message">Unable to load cars. Check the server connection.</p>';
            });
    }

    function loadBrands() {

        if (!brandGrid) return;

        brandGrid.innerHTML = "";

        fetch("BrandServ?action=list")
            .then(function(response) {

                if (!response.ok) {
                    throw new Error("BrandServlet returned " + response.status);
                }

                return response.json();
            })
            .then(function(brands) {

                if (!Array.isArray(brands)) {
                    throw new Error(brands.error || "Invalid brand response");
                }

                allBrands = brands;
                brandGrid.innerHTML = "";

                brands.forEach(function(brand) {

                    const button = document.createElement("button");
                    button.type = "button";
                    button.className = "brand-card";
                    button.dataset.brandId = brand.brandId;
                    const image = document.createElement("img");
                    image.src = assetPath(brand.logo, "brands");
                    image.alt = brand.brandName || "Brand";

                    image.onerror = function() {
                        this.style.visibility = "hidden";
                    };

                    const name = document.createElement("span");
                    name.textContent = brand.brandName || "Brand";
                    button.append(image, name);
                    brandGrid.appendChild(button);
                });
            })
            .catch(function(error) {

                console.error("Brand loading error:", error);

                brandGrid.innerHTML =
                    '<p class="no-cars-message">Unable to load brands.</p>';
            });
    }

    tabs.forEach(function(tab) {

        tab.addEventListener("click", function() {

            tabs.forEach(function(item) {
                item.classList.remove("active");
            });

            tab.classList.add("active");

            activeCategory =
                tab.getAttribute("data-category") || "popular";

            moveIndicator(tab, true);
            displayCars();
        });
    });
    if (brandGrid) {

        brandGrid.addEventListener("click", function(event) {

            const card = event.target.closest(".brand-card");

            if (!card) return;

            const brandId = Number(card.dataset.brandId);

            selectedBrandId =
                selectedBrandId === brandId ? null : brandId;

            brandGrid.querySelectorAll(".brand-card").forEach(function(item) {

                item.classList.toggle(
                    "active",
                    Number(item.dataset.brandId) === selectedBrandId
                );
            });

            displayCars();

            const popularSection = document.querySelector(".popular-cars");

            if (popularSection) {
                popularSection.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            }
        });
    }

    if (carSearchInput && searchSuggestions) {
        let currentSuggestions = [];
        let activeSuggestionIndex = -1;

        const colourMap = {
            white: "#ffffff",
            black: "#111111",
            red: "#c62828",
            blue: "#2864a7",
            grey: "#808080",
            gray: "#808080",
            silver: "#c0c0c0",
            green: "#39804b",
            brown: "#795548",
            orange: "#e87525",
            yellow: "#fdd835",
            beige: "#f5f5dc",
            gold: "#c9a227",
            purple: "#70459b",
            pink: "#ff80c0"
        };

        function findMatchingCars(term) {
            const query = term.toLowerCase().trim();
            if (!query) return [];

            return allCars.filter(function(car) {
                return String(car.carName || "").toLowerCase().includes(query) ||
                    String(car.brandName || "").toLowerCase().includes(query);
            });
        }

        function getCarColours(car) {
            const rawColours = car.availableColours ||
                car.availableColors ||
                car.colours ||
                car.colors ||
                car.colour ||
                car.color ||
                [];

            if (Array.isArray(rawColours)) return rawColours;

            return String(rawColours)
                .split(",")
                .map(function(colour) {
                    return colour.trim();
                })
                .filter(Boolean);
        }

        function displaySuggestions() {
            const term = carSearchInput.value.trim();
            searchSuggestions.innerHTML = "";
            currentSuggestions = [];
            activeSuggestionIndex = -1;

            if (!term) {
                searchSuggestions.hidden = true;
                carSearchInput.setAttribute("aria-expanded", "false");
                return;
            }

            const matches = findMatchingCars(term);

            matches.sort(function(a, b) {
                const nameA = String(a.carName || "").toLowerCase();
                const nameB = String(b.carName || "").toLowerCase();
                const startsA = nameA.startsWith(term.toLowerCase()) ? 0 : 1;
                const startsB = nameB.startsWith(term.toLowerCase()) ? 0 : 1;
                return startsA - startsB || nameA.localeCompare(nameB);
            });

            currentSuggestions = matches.slice(0, 6);
            searchSuggestions.hidden = false;
            carSearchInput.setAttribute("aria-expanded", "true");

            if (!currentSuggestions.length) {
                const message = document.createElement("div");
                message.className = "no-search-suggestions";
                message.textContent = "No matching cars found.";
                searchSuggestions.appendChild(message);
                return;
            }

            currentSuggestions.forEach(function(car) {
                const button = document.createElement("button");
                button.type = "button";
                button.className = "search-suggestion";
                button.setAttribute("role", "option");
                button.setAttribute("aria-selected", "false");

                const info = document.createElement("div");
                info.className = "suggestion-info";

                const titleRow = document.createElement("div");
                titleRow.className = "suggestion-title-row";

                const name = document.createElement("span");
                name.className = "suggestion-name";
                name.textContent = car.carName || "Unnamed car";

                const colourContainer = document.createElement("span");
                colourContainer.className = "suggestion-colours";

                getCarColours(car).forEach(function(colour) {
                    const swatch = document.createElement("span");
                    const normalizedColour = String(colour).trim().toLowerCase();

                    swatch.className = "suggestion-colour-swatch";
                    swatch.style.backgroundColor =
                        colourMap[normalizedColour] ||
                        (/^#[0-9a-f]{3,8}$/i.test(normalizedColour)
                            ? normalizedColour
                            : "#cccccc");

                    swatch.title = colour;
                    colourContainer.appendChild(swatch);
                });

                titleRow.append(name, colourContainer);

                const brand = document.createElement("span");
                brand.className = "suggestion-brand";
                brand.textContent = car.brandName || "Brand unavailable";

                const price = document.createElement("span");
                price.className = "suggestion-price";
                price.textContent = formatPrice(car);

                info.append(titleRow, brand);
                button.append(info, price);

                button.addEventListener("click", function() {
                    selectSearchResult(car);
                });

                searchSuggestions.appendChild(button);
            });
        }

        function showSearchResults(cars) {
            carGrid.innerHTML = "";

            if (!cars.length) {
                carGrid.innerHTML =
                    '<p class="no-cars-message">No matching cars found.</p>';
            } else {
                cars.forEach(function(car) {
                    carGrid.appendChild(createCarCard(car));
                });
            }

            const popularSection = document.querySelector(".popular-cars");

            if (popularSection) {
                popularSection.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            }
        }

        function selectSearchResult(car) {
            carSearchInput.value = car.carName;
            searchSuggestions.innerHTML = "";
            searchSuggestions.hidden = true;
            carSearchInput.setAttribute("aria-expanded", "false");
            currentSuggestions = [];
            activeSuggestionIndex = -1;
            showSearchResults([car]);
        }

        function executeSearch() {
            const term = carSearchInput.value.trim();

            if (!term) {
                displayCars();
                searchSuggestions.hidden = true;
                carSearchInput.setAttribute("aria-expanded", "false");
                return;
            }

            const matches = findMatchingCars(term);
            searchSuggestions.hidden = true;
            carSearchInput.setAttribute("aria-expanded", "false");
            showSearchResults(matches);
        }

        function updateActiveSuggestion() {
            const buttons = searchSuggestions.querySelectorAll(".search-suggestion");

            buttons.forEach(function(button, index) {
                const active = index === activeSuggestionIndex;
                button.classList.toggle("active", active);
                button.setAttribute("aria-selected", String(active));

                if (active) {
                    button.scrollIntoView({ block: "nearest" });
                }
            });
        }

        carSearchInput.addEventListener("input", displaySuggestions);

        carSearchInput.addEventListener("keydown", function(event) {
            if (event.key === "ArrowDown") {
                if (searchSuggestions.hidden) displaySuggestions();

                if (currentSuggestions.length) {
                    event.preventDefault();
                    activeSuggestionIndex =
                        (activeSuggestionIndex + 1) % currentSuggestions.length;
                    updateActiveSuggestion();
                }
            } else if (event.key === "ArrowUp") {
                if (currentSuggestions.length) {
                    event.preventDefault();
                    activeSuggestionIndex =
                        activeSuggestionIndex <= 0
                            ? currentSuggestions.length - 1
                            : activeSuggestionIndex - 1;
                    updateActiveSuggestion();
                }
            } else if (event.key === "Enter") {
                event.preventDefault();

                if (activeSuggestionIndex >= 0 &&
                    currentSuggestions[activeSuggestionIndex]) {
                    selectSearchResult(currentSuggestions[activeSuggestionIndex]);
                } else {
                    executeSearch();
                }
            } else if (event.key === "Escape") {
                searchSuggestions.hidden = true;
                carSearchInput.setAttribute("aria-expanded", "false");
                activeSuggestionIndex = -1;
            }
        });

        if (searchIcon) {
            searchIcon.addEventListener("click", executeSearch);
        }

        document.addEventListener("click", function(event) {
            if (!event.target.closest(".search-bar")) {
                searchSuggestions.hidden = true;
                carSearchInput.setAttribute("aria-expanded", "false");
                activeSuggestionIndex = -1;
            }
        });
    }
	
	carGrid.addEventListener("click", function(event) {

	    if (event.target.closest(".wishlist-button, .compare-button")) {
	        return;
	    }

	    const card = event.target.closest(".car-card");

	    if (!card || !card.dataset.carId) return;

	    window.location.href =
	        "car-details.html?id=" + encodeURIComponent(card.dataset.carId);
	});
	
	document.addEventListener("click", function(event) {
	    const compare = event.target.closest(".compare-button");

	    if (compare) {
	        event.stopPropagation();
	        compare.classList.toggle("added");
	    }
	});

    const sidebarToggle = document.getElementById("sidebarToggle");
    const sidebar = document.getElementById("sidebar");
    const sidebarClose = document.getElementById("sidebarClose");
    const sidebarOverlay = document.getElementById("sidebarOverlay");

    function closeSidebar() {

        if (sidebar) sidebar.classList.remove("open");
        if (sidebarOverlay) sidebarOverlay.classList.remove("open");
    }

    if (sidebarToggle) {

        sidebarToggle.addEventListener("click", function() {

            if (sidebar) sidebar.classList.add("open");
            if (sidebarOverlay) sidebarOverlay.classList.add("open");
        });
    }

    if (sidebarClose) {
        sidebarClose.addEventListener("click", closeSidebar);
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener("click", closeSidebar);
    }

    document.addEventListener("keydown", function(event) {
        if (event.key === "Escape") {
            if (sidebar) {
                closeSidebar();
            }
        }
    });

    const initialTab = document.querySelector(".car-tab.active");

    if (initialTab) {

        activeCategory =
            initialTab.getAttribute("data-category") || "popular";

        moveIndicator(initialTab, false);
    }
    window.addEventListener("resize", function() {

        const activeTab = document.querySelector(".car-tab.active");
        if (activeTab) {
            moveIndicator(activeTab, false);
        }
    });

    function initializeHorizontalSlider(wrapperSelector, scrollSelector) {

        const wrapper = document.querySelector(wrapperSelector);

        const scrollArea = wrapper
            ? wrapper.querySelector(scrollSelector)
            : null;

        if (!wrapper || !scrollArea) return;

        const leftArrow = wrapper.querySelector(".slider-arrow.left");
        const rightArrow = wrapper.querySelector(".slider-arrow.right");

        if (!leftArrow || !rightArrow) return;

        function updateArrows() {

            const hasHorizontalOverflow =
                scrollArea.scrollWidth > scrollArea.clientWidth + 2;

            leftArrow.hidden =
                !hasHorizontalOverflow || scrollArea.scrollLeft <= 2;

            rightArrow.hidden =
                !hasHorizontalOverflow ||
                scrollArea.scrollLeft + scrollArea.clientWidth >=
                scrollArea.scrollWidth - 2;
        }

        leftArrow.addEventListener("click", function() {

            scrollArea.scrollBy({
                left: -Math.max(300, scrollArea.clientWidth * 0.75),
                behavior: "smooth"
            });
        });

        rightArrow.addEventListener("click", function() {

            scrollArea.scrollBy({
                left: Math.max(300, scrollArea.clientWidth * 0.75),
                behavior: "smooth"
            });
        });

        scrollArea.addEventListener("scroll", updateArrows, {
            passive: true
        });

        window.addEventListener("resize", updateArrows);

        const contentGrid =
            scrollArea.querySelector(".car-grid, .brand-grid");

        if (contentGrid) {

            const observer = new MutationObserver(function() {
                requestAnimationFrame(updateArrows);
            });

            observer.observe(contentGrid, {
                childList: true,
                subtree: true
            });
        }

        scrollArea.addEventListener("load", updateArrows, true);

        requestAnimationFrame(updateArrows);

        return updateArrows;
    }

    initializeHorizontalSlider(
        ".car-slider-wrapper",
        ".car-scroll"
    );

    initializeHorizontalSlider(
        ".brand-slider-wrapper",
        ".brand-scroll"
    );

    loadCars();
    loadBrands();

});