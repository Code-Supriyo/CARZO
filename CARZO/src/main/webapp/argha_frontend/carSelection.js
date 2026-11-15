
document.addEventListener("DOMContentLoaded", () => {

    const contextPath = window.location.pathname.substring(
        0, window.location.pathname.indexOf("/", 1)
    );

    const grid = document.getElementById("carGrid");
    const searchForm = document.getElementById("searchForm");
    const searchInput = document.getElementById("carSearch");
    const category = document.getElementById("category");
    const count = document.getElementById("selectedCount");
    const compareButton = document.getElementById("compareButton");
    const selectAllButton = document.getElementById("selectAllButton");
    const clearButton = document.getElementById("clearSelectionButton");
    const compareForm = document.getElementById("compareForm");
    const error = document.getElementById("errorMessage");

    let cars = [];
    let selected = new Set();

    function showError(message) {
        error.textContent = message;
        error.hidden = false;
    }

    function clearError() {
        error.textContent = "";
        error.hidden = true;
    }

    function updateSelection() {
        count.textContent = selected.size;
        compareButton.disabled =
            selected.size < 2 || selected.size > 4;

        grid.querySelectorAll(".compare-select").forEach(box => {
            box.checked = selected.has(Number(box.value));
        });
    }

    function renderCars() {

        grid.replaceChildren();

        if (!cars.length) {
            grid.innerHTML =
                '<p class="no-data">No cars found.</p>';
            return;
        }

        cars.forEach(car => {

            const card = document.createElement("article");
            card.className = "car-card";

            const image = document.createElement("img");
            image.className = "car-image";
            image.src = car.imageUrl || "";
            image.alt = car.carName;
            image.onerror = () => {
                image.style.display = "none";
            };

            const content = document.createElement("div");
            content.className = "car-content";

            const brand = document.createElement("p");
            brand.className = "brand-name";
            brand.textContent = car.brandName || "";

            const name = document.createElement("h3");
            name.className = "car-name";
            name.textContent = car.carName;

            const price = document.createElement("p");
            price.className = "car-price";
            price.textContent =
                "₹" + car.minPrice + " - ₹" + car.maxPrice + " Lakh";

            const specs = document.createElement("p");
            specs.textContent =
                `${car.mileage} km/l | ${car.engineCapacity} cc | ${car.airbags} airbags`;

            const label = document.createElement("label");
            label.className = "compare-label";

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.className = "compare-select";
            checkbox.value = car.carId;
            checkbox.checked = selected.has(car.carId);

            checkbox.addEventListener("change", () => {

                if (checkbox.checked) {
                    if (selected.size >= 4) {
                        checkbox.checked = false;
                        showError("You can select a maximum of 4 cars.");
                        return;
                    }

                    selected.add(car.carId);
                } else {
                    selected.delete(car.carId);
                }

                clearError();
                updateSelection();
            });

            label.append(checkbox, document.createTextNode(" Compare"));

            content.append(brand, name, price, specs, label);
            card.append(image, content);
            grid.append(card);
        });

        updateSelection();
    }

    async function loadCars() {

        clearError();
        grid.innerHTML =
            '<p class="no-data">Loading cars...</p>';

        const params = new URLSearchParams();

        if (searchInput.value.trim()) {
            params.set("search", searchInput.value.trim());
        }

        if (category.value) {
            params.set("category", category.value);
        }

        try {
            const response = await fetch(
                contextPath + "/CarSelectionServlet?" + params
            );

            if (!response.ok) throw new Error("Unable to load cars.");

            cars = await response.json();

            if (!Array.isArray(cars)) {
                throw new Error("Invalid server response.");
            }

            selected = new Set(
                [...selected].filter(id =>
                    cars.some(car => car.carId === id)
                )
            );

            renderCars();

        } catch (e) {
            showError(e.message);
            grid.innerHTML =
                '<p class="no-data">Unable to load cars.</p>';
        }
    }

    searchForm.addEventListener("submit", e => {
        e.preventDefault();
        loadCars();
    });

    selectAllButton.addEventListener("click", () => {

        selected.clear();

        cars.slice(0, 4).forEach(car => {
            selected.add(car.carId);
        });

        clearError();
        updateSelection();
    });

    clearButton.addEventListener("click", () => {
        selected.clear();
        clearError();
        updateSelection();
    });

    compareForm.addEventListener("submit", async e => {

        e.preventDefault();

        if (selected.size < 2 || selected.size > 4) {
            showError("Select between 2 and 4 cars.");
            return;
        }

        const data = new URLSearchParams();

        selected.forEach(id => data.append("carIds", id));

        try {
            const response = await fetch(
                contextPath + "/CompareCarsServlet",
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body: data.toString()
                }
            );

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Comparison failed.");
            }

            window.location.href =
                contextPath + "/compareCars.html";

        } catch (e) {
            showError(e.message);
        }
    });

    loadCars();
});
