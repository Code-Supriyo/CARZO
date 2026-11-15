document.addEventListener("DOMContentLoaded", () => {
	const params = new URLSearchParams(window.location.search);
	const bookingStatus = params.get("booking");

	if (bookingStatus === "success") {
	    alert("Car booking done! Feel free to review your details through purchase history.");
	} else if (bookingStatus === "already-exists") {
	    alert("You have already booked this car. You can review your booking details through Purchase History.");
	}

	if (bookingStatus) {
	    params.delete("booking");

	    const newUrl = params.toString()
	        ? `${window.location.pathname}?${params.toString()}`
	        : window.location.pathname;

	    window.history.replaceState({}, document.title, newUrl);
	}

    const carId = params.get("id");
	setupSidebar();
    if (!carId || !/^\d+$/.test(carId)) {
        showError("Invalid car selection.");
        return;
    }

    loadCarDetails(carId);
    setupNavigation();
    setupBuyNowButton(carId);
});

function setupSidebar() {
    const sidebarToggle = document.getElementById("sidebarToggle");
    const sidebar = document.getElementById("sidebar");
    const sidebarClose = document.getElementById("sidebarClose");
    const sidebarOverlay = document.getElementById("sidebarOverlay");

    function openSidebar() {
        sidebar?.classList.add("open");
        sidebarOverlay?.classList.add("open");
    }

    function closeSidebar() {
        sidebar?.classList.remove("open");
        sidebarOverlay?.classList.remove("open");
    }

    sidebarToggle?.addEventListener("click", openSidebar);
    sidebarClose?.addEventListener("click", closeSidebar);
    sidebarOverlay?.addEventListener("click", closeSidebar);

    document.addEventListener("keydown", event => {
        if (event.key === "Escape") {
            closeSidebar();
        }
    });
}

async function loadCarDetails(carId) {
    try {
        const response = await fetch(
            `CarDetailsServ?id=${encodeURIComponent(carId)}`
        );

        if (!response.ok) {
            throw new Error("Unable to retrieve car details.");
        }

        const car = await response.json();

        if (!car || !car.carId) {
            throw new Error("Car not found.");
        }

        displayCarDetails(car);

    } catch (error) {
        console.error("Car details error:", error);
        showError("Unable to load car details. Please try again.");
    }
}

function displayCarDetails(car) {
    const name = car.carName || "Car Details";
    document.title = `${name} | CARZO`;
	
	const wishlistButton = document.getElementById("wishlistBtn");

	if (wishlistButton) {
	    wishlistButton.dataset.carId = car.carId;
	    if (window.CARZO_WISHLIST) {
	        window.CARZO_WISHLIST.syncHearts();
	    }
	}
	
    setText("carName", name);
    setText("priceCarName", name);
    setText("overviewDescription",car.description || "No description available.");
    const image = document.getElementById("carImage");

    if (image) {
        if (car.imageUrl) {
            image.src = car.imageUrl;
            image.alt = name;
        } else {
            image.alt = "Car image unavailable";
        }

        image.onerror = () => {
            image.style.display = "none";
        };
    }

    const minPrice = formatPrice(car.minPrice);
    const maxPrice = formatPrice(car.maxPrice);

    let price = "Price not available";

    if (minPrice && maxPrice) {
        price = `Rs. ${minPrice} - ${maxPrice} Lakh`;
    } else if (minPrice) {
        price = `Rs. ${minPrice} Lakh onwards`;
    }

    setText("carPrice", price);
    setText("categoryName", car.categoryName);
    setText("fuelType", formatList(car.fuelTypes));
    setText("transmission", car.transmission);
    setText("mileage", car.mileage ? `${car.mileage} km/l` : "—");
    setText(
        "engineCapacity",
        car.engineCapacity ? `${car.engineCapacity} cc` : "—"
    );
    setText(
        "seatingCapacity",
        car.seatingCapacity ? `${car.seatingCapacity} Seats` : "—"
    );
    setText(
        "airbags",
        car.airbags != null ? `${car.airbags} Airbags` : "—"
    );

    displayColours(car.availableColours);
    displaySimilarCars(car.similarCars || []);
}

function displaySimilarCars(cars) {
    const section = document.getElementById("similar-cars");

    if (!section) return;

    section.innerHTML = "";

    const heading = document.createElement("h2");
    heading.textContent = "Similar Cars";
    section.appendChild(heading);

    if (!Array.isArray(cars) || cars.length === 0) {
        const emptyMessage = document.createElement("p");
        emptyMessage.className = "section-placeholder";
        emptyMessage.textContent = "No similar cars found.";
        section.appendChild(emptyMessage);
        return;
    }

    const slider = document.createElement("div");
    slider.className = "horizontal-slider car-slider-wrapper";

    const leftArrow = document.createElement("button");
    leftArrow.type = "button";
    leftArrow.className = "slider-arrow left";
    leftArrow.setAttribute("aria-label", "Scroll similar cars left");
    leftArrow.innerHTML = "&#10094;";

    const scroll = document.createElement("div");
    scroll.className = "car-scroll";
    scroll.id = "similarCarsScroll";

    const grid = document.createElement("div");
    grid.className = "car-grid";

    cars.forEach(car => {
        grid.appendChild(createSimilarCarCard(car));
    });

    scroll.appendChild(grid);

    const rightArrow = document.createElement("button");
    rightArrow.type = "button";
    rightArrow.className = "slider-arrow right";
    rightArrow.setAttribute("aria-label", "Scroll similar cars right");
    rightArrow.innerHTML = "&#10095;";

    leftArrow.addEventListener("click", () => {
        scroll.scrollBy({
            left: -314,
            behavior: "smooth"
        });
    });

    rightArrow.addEventListener("click", () => {
        scroll.scrollBy({
            left: 314,
            behavior: "smooth"
        });
    });

    slider.append(leftArrow, scroll, rightArrow);
    section.appendChild(slider);
}

function createSimilarCarCard(car) {
    const card = document.createElement("article");
    card.dataset.carId = car.carId ?? car.id;
    card.className = "car-card";

    const imageUrl = car.imageUrl || car.image || "";
    const carName = car.carName || car.name || "Car";
    const minPrice = formatPrice(car.minPrice);
    const maxPrice = formatPrice(car.maxPrice);

    let price = "Price not available";

    if (minPrice && maxPrice) {
        price = `₹${minPrice} - ₹${maxPrice} Lakh`;
    } else if (minPrice) {
        price = `₹${minPrice} Lakh onwards`;
    }

    card.innerHTML = `
        <div class="car-image">
            <img
                src="${escapeHtml(imageUrl)}"
                alt="${escapeHtml(carName)}"
            >

            <button
                type="button"
                class="wishlist-button"
                aria-label="Add to wishlist"
            >
                <img
                    src="heart-svgrepo-com.svg"
                    alt="Wishlist"
                    class="heart-outline"
                >

                <img
                    src="heart-fill-svgrepo-com.svg"
                    alt="Wishlisted"
                    class="heart-filled"
                >
            </button>
        </div>

        <div class="car-details">
            <h3>${escapeHtml(carName)}</h3>
            <p>${escapeHtml(price)}</p>

            <button type="button" class="compare-button">
                Buy Now
            </button>
        </div>
    `;

    const image = card.querySelector(".car-image > img");

    image.addEventListener("error", () => {
        image.style.display = "none";
    });

    card.addEventListener("click", (event) => {
        if (event.target.closest(".wishlist-button, .compare-button")) {
            return;
        }

        window.location.href =
            `car-details.html?id=${encodeURIComponent(card.dataset.carId)}`;
    });
	
	const buyNowButton = card.querySelector(".compare-button");

	buyNowButton.addEventListener("click", event => {
	    event.stopPropagation();
	    window.location.href =
	        `confirm-purchase.html?id=${encodeURIComponent(card.dataset.carId)}`;
	});
	
    card.style.cursor = "pointer";
    return card;
}

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[character]);
}

function displayColours(colours) {
    const container = document.getElementById("colourSwatches");
    const selectedLabel = document.getElementById("selectedColour");

    if (!container) return;

    container.innerHTML = "";

    const colourList = Array.isArray(colours)
        ? colours
        : typeof colours === "string"
            ? colours.split(",").map(colour => colour.trim()).filter(Boolean)
            : [];

    if (colourList.length === 0) {
        container.textContent = "Colour information unavailable.";
        return;
    }

    const colourCodes = {
        "white": "#f5f5f5",
        "black": "#171717",
        "red": "#c62828",
        "blue": "#2456a6",
        "silver": "#bfc3c7",
        "grey": "#777777",
        "gray": "#777777",
        "brown": "#795548",
        "green": "#39734a",
        "orange": "#e88732",
        "yellow": "#e7c744",
        "gold": "#c9a34a",
        "beige": "#d8c6a5",
        "maroon": "#701c32"
    };

    colourList.forEach((colour, index) => {
        const swatch = document.createElement("button");
        const colourKey = colour.toLowerCase();

        swatch.type = "button";
        swatch.className = "colour-swatch";
        swatch.style.backgroundColor =
            colourCodes[colourKey] || "#cccccc";

        swatch.title = colour;
        swatch.setAttribute("aria-label", colour);

        if (index === 0) {
            swatch.classList.add("selected");

            if (selectedLabel) {
                selectedLabel.textContent = colour;
            }
        }

        swatch.addEventListener("click", () => {
            container.querySelectorAll(".colour-swatch").forEach(item => {
                item.classList.remove("selected");
            });

            swatch.classList.add("selected");

            if (selectedLabel) {
                selectedLabel.textContent = colour;
            }
        });

        container.appendChild(swatch);
    });
}

function setupNavigation() {
    const links = document.querySelectorAll(".nav-link");

    links.forEach(link => {
        link.addEventListener("click", () => {
            links.forEach(item => item.classList.remove("active"));
            link.classList.add("active");
        });
    });
}

function setupBuyNowButton(carId) {
    const button = document.getElementById("buyNowBtn");

    if (!button) return;

    button.addEventListener("click", () => {
        window.location.href =`confirm-purchase.html?id=${encodeURIComponent(carId)}`;
    });
}

function formatPrice(value) {
    if (value === null || value === undefined || value === "") {
        return null;
    }

    return Number(value).toFixed(2);
}

function formatList(value) {
    if (Array.isArray(value)) {
        return value.join(", ") || "—";
    }

    return value || "—";
}

function setText(id, value) {
    const element = document.getElementById(id);

    if (element) {
        element.textContent =
            value === null || value === undefined || value === ""
                ? "—"
                : value;
    }
}

function showError(message) {
    setText("carName", message);
    setText("overviewDescription", message);
    setText("carPrice", "—");

    const colourContainer = document.getElementById("colourSwatches");

    if (colourContainer) {
        colourContainer.textContent = "—";
    }
}
