
document.addEventListener("DOMContentLoaded", function () {

    const filterButtons = document.querySelectorAll(".filter-button");
    const filterModal = document.getElementById("filterModal");
    const filterClose = document.getElementById("filterClose");
    const filterForm = document.getElementById("filterForm");
    const clearFilters = document.getElementById("clearFilters");
    const filterGroups = document.querySelectorAll(".filter-group");
    const resultsSection = document.getElementById("filteredResults");
    const resultsGrid = document.getElementById("resultsGrid");
    const resultCount = document.getElementById("resultCount");
    const resultSort = document.getElementById("resultSort");
	if (resultSort) {
	    resultSort.value = "name";
	}
	const selectedFilters = document.getElementById("selectedFilters");
    const popularSection = document.querySelector(".popular-cars");
    const brandsSection = document.querySelector(".top-brands");


    // OPEN FILTER MODAL

    function openFilterModal(filterName) {

        if (!filterModal) return;

        filterModal.classList.add("open");

        filterGroups.forEach(function (group) {
            group.classList.remove("open");
        });

        if (filterName && filterName !== "all") {

            const target = document.querySelector(
                '.filter-group[data-filter-group="' + filterName + '"]'
            );

            if (target) {

                target.classList.add("open");

                setTimeout(function () {
                    target.scrollIntoView({
                        behavior: "smooth",
                        block: "nearest"
                    });
                }, 100);

            }
        }
    }


    // FILTER BUTTONS

    filterButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            openFilterModal(
                button.getAttribute("data-filter") || "all"
            );
        });
    });

    // CLOSE FILTER MODAL

    if (filterClose && filterModal) {

        filterClose.addEventListener("click", function () {
            filterModal.classList.remove("open");
        });
    }

    if (filterModal) {

        filterModal.addEventListener("click", function (event) {

            if (event.target === filterModal) {
                filterModal.classList.remove("open");
            }
        });
    }

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && filterModal) {
            filterModal.classList.remove("open");
        }
    });

    // FILTER GROUP ACCORDION

    filterGroups.forEach(function (group) {

        const header = group.querySelector(".filter-group-header");

        if (header) {

            header.addEventListener("click", function () {
                group.classList.toggle("open");
            });
        }
    });

    // COLLECT SELECTED FILTERS

    function collectFilters() {

        const formData = new FormData(filterForm);
        const filters = {};

        formData.forEach(function (value, key) {

            if (filters[key] === undefined) {

                filters[key] = value;

            } else if (Array.isArray(filters[key])) {

                filters[key].push(value);

            } else {
                filters[key] = [filters[key], value];
            }
        });
        return filters;
    }

	// DISPLAY SELECTED FILTERS AS REMOVABLE CHIPS

	function renderSelectedFilters() {

	    if (!selectedFilters || !filterForm) return;

	    selectedFilters.innerHTML = "";
	    function createChip(label, removeCallback) {

	        const chip = document.createElement("div");
	        chip.className = "filter-chip";
	        const text = document.createElement("span");
	        text.textContent = label;
	        const removeButton = document.createElement("button");
	        removeButton.type = "button";
	        removeButton.className = "remove-filter";
	        removeButton.textContent = "×";
	        removeButton.setAttribute("aria-label", "Remove " + label);
			
	        removeButton.addEventListener("click", function () {
	            removeCallback();
	            renderSelectedFilters();
	            filterForm.requestSubmit();
	        });
	        chip.append(text, removeButton);
	        selectedFilters.appendChild(chip);
	    }

	    // BUDGET FILTER

	    const minPrice = filterForm.querySelector('[name="minPrice"]');
	    const maxPrice = filterForm.querySelector('[name="maxPrice"]');

	    const min = minPrice ? minPrice.value.trim() : "";
	    const max = maxPrice ? maxPrice.value.trim() : "";

	    if (min || max) {
	        let budgetLabel;

	        if (min && max) {
	            budgetLabel = "₹" + min + " - ₹" + max + " Lakh";
	        } else if (min) {
	            budgetLabel = "Min ₹" + min + " Lakh";
	        } else {
	            budgetLabel = "Up to ₹" + max + " Lakh";
	        }

	        createChip(budgetLabel, function () {

	            if (minPrice) minPrice.value = "";
	            if (maxPrice) maxPrice.value = "";

	        });
	    }
		
	    const checkedInputs = filterForm.querySelectorAll(
	        'input[type="checkbox"]:checked'
	    );

	    checkedInputs.forEach(function (input) {

	        const labelElement = input.closest("label");

	        let label = labelElement
	            ? labelElement.textContent.trim().replace(/\s+/g, " ")
	            : input.value;

	        createChip(label, function () {
	            input.checked = false;
	        });

	    });

	}

    // DISPLAY FILTERED CAR RESULTS

	function showResults(cars) {
	    resultsGrid.innerHTML = "";
	    resultCount.textContent = cars.length + (cars.length === 1 ? " Car" : " Cars");
		
	    if (!cars.length) {
	        resultsGrid.innerHTML =
	            '<p class="results-empty">No cars match your selected filters. Try changing your filters.</p>';
	    } else {

			const sortMode = resultSort ? resultSort.value : "name";
			const sortedCars = [...cars].sort(function (a, b) {

			    if (sortMode === "price-low") {
			        return Number(a.minPrice ?? Infinity) -
			               Number(b.minPrice ?? Infinity);
			    }

			    if (sortMode === "price-high") {
			        return Number(b.minPrice ?? -Infinity) -
			               Number(a.minPrice ?? -Infinity);
			    }

			    return (a.carName || "").trim().localeCompare(
			        (b.carName || "").trim(),
			        "en",
			        { sensitivity: "base" }
			    );

			});

	        sortedCars.forEach(function (car) {
	            const card = document.createElement("article");
	            card.className = "result-car-card";
	            const imageWrap = document.createElement("div");
	            imageWrap.className = "result-car-image";
	            const image = document.createElement("img");
	            image.src = car.imageUrl || "";
	            image.alt = car.carName || "Car";
	            image.onerror = function () { this.style.visibility = "hidden"; };

	            imageWrap.appendChild(image);
	            const details = document.createElement("div");
	            details.className = "result-car-details";
	            const name = document.createElement("h3");
	            name.textContent = car.carName || "Unnamed car";
	            const price = document.createElement("p");
	            price.className = "result-price";

	            if (car.minPrice != null && car.maxPrice != null) {
	                price.textContent =
	                    "₹" + Number(car.minPrice).toFixed(2) +
	                    " - ₹" + Number(car.maxPrice).toFixed(2) + " Lakh";

	            } else if (car.minPrice != null) {
	                price.textContent =
	                    "From ₹" + Number(car.minPrice).toFixed(2) + " Lakh";
						
	            } else if (car.maxPrice != null) {

	                price.textContent =
	                    "Up to ₹" + Number(car.maxPrice).toFixed(2) + " Lakh";

	            } else {
	                price.textContent = "Price not available";
	            }

	            const description = document.createElement("p");
	            description.className = "result-description";
	            description.textContent =
	                car.description ||
	                car.brandName ||
	                car.categoryName ||
	                "";

	            details.append(name, price, description);
	            card.append(imageWrap, details);
	            resultsGrid.appendChild(card);
	        });
			sortVisibleResults();
	    }

	    if (resultsSection) {
	        resultsSection.hidden = false;
	    }

	    if (popularSection) {
	        popularSection.hidden = true;
	    }

	    if (brandsSection) {
	        brandsSection.hidden = true;
	    }

	    if (resultsSection) {
	        resultsSection.scrollIntoView({
	            behavior: "smooth",
	            block: "start"
	        });
	    }
	}


    // SORT FILTERED RESULTS

    function sortVisibleResults() {

        if (!resultSort || !resultsGrid) return;

        const cards = Array.from(
            resultsGrid.querySelectorAll(".result-car-card")
        );

        const mode = resultSort.value;


        cards.sort(function (a, b) {

            const nameA =
                a.querySelector("h3").textContent.toLowerCase();

            const nameB =
                b.querySelector("h3").textContent.toLowerCase();


            const priceA = Number(
                (a.querySelector(".result-price").textContent.match(/[\d.]+/) || [0])[0]
            );

            const priceB = Number(
                (b.querySelector(".result-price").textContent.match(/[\d.]+/) || [0])[0]
            );


            if (mode === "price-low") {
                return priceA - priceB;
            }

            if (mode === "price-high") {
                return priceB - priceA;
            }

            if (mode === "name") {
                return nameA.localeCompare(nameB);
            }

            return 0;

        });

        cards.forEach(function (card) {
            resultsGrid.appendChild(card);
        });

    }

    if (resultSort) {
		resultSort.addEventListener("change", function () {
		    sortVisibleResults();
		});    }

    // CLEAR FILTERS

    if (clearFilters && filterForm) {

        clearFilters.addEventListener("click", function () {

            filterForm.reset();
			selectedFilters.innerHTML = "";

            if (resultsSection) {
                resultsSection.hidden = true;
            }

            if (popularSection) {
                popularSection.hidden = false;
            }

            if (brandsSection) {
                brandsSection.hidden = false;
            }

        });

    }

    // APPLY FILTERS

    if (filterForm) {

        filterForm.addEventListener("submit", function (event) {

            event.preventDefault();
            const filters = collectFilters();
			console.log("Filters being sent to Servlet:", filters);
			
            const minPrice =
                filters.minPrice === "" ? null : Number(filters.minPrice);

            const maxPrice =
                filters.maxPrice === "" ? null : Number(filters.maxPrice);


            // BUDGET VALIDATION

            if (
                (minPrice !== null && (minPrice < 2 || minPrice > 100)) ||
                (maxPrice !== null && (maxPrice < 2 || maxPrice > 100))
            ) {

                alert("Budget must be between ₹2 lakh and ₹1 crore.");
                return;

            }

            if (
                minPrice !== null &&
                maxPrice !== null &&
                minPrice > maxPrice
            ) {
                alert("Minimum price cannot be greater than maximum price.");
                return;
            }

            // LOADING MESSAGE

            if (resultsGrid) {
                resultsGrid.innerHTML =
                    '<p class="results-loading">Finding matching cars...</p>';
            }

            if (resultsSection) {
                resultsSection.hidden = false;
            }

            // SEND FILTER REQUEST TO SERVLET

            fetch("CarServ?action=filter", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(filters)

            })

            // READ AND DEBUG SERVER RESPONSE

            .then(async function (response) {
                const text = await response.text();
                console.log("HTTP Status:", response.status);
                console.log("Raw server response:", text);
                let data;

                try {
                    data = JSON.parse(text);

                } catch (e) {
                    throw new Error(
                        "Server returned invalid JSON: " + text
                    );
                }
				
                if (!response.ok) {
                    throw new Error(
                        data.error ||
                        "HTTP Error: " + response.status
                    );
                }

                if (!Array.isArray(data)) {

                    throw new Error(
                        data.error || "Invalid filter response"
                    );
                }
                return data;
            })

            .then(function (cars) {
                showResults(cars);
				renderSelectedFilters();

                if (filterModal) {
                    filterModal.classList.remove("open");
                }
            })

            .catch(function (error) {
                console.error("Filter error:", error);

                if (resultsGrid) {
                    resultsGrid.innerHTML =
                        '<p class="results-error">Unable to fetch filtered cars. Check the browser console for details.</p>';
                }

                if (resultsSection) {
                    resultsSection.hidden = false;
                }
            });
        });
    }
});
