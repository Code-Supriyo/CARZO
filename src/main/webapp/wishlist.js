
document.addEventListener("DOMContentLoaded", loadWishlist);

async function loadWishlist() {
    const grid = document.getElementById("wishlistGrid");
    const message = document.getElementById("wishlistMessage");

    try {
        const response = await fetch("WishlistServ?action=list", {
            cache: "no-store"
        });

        if (!response.ok) {
            throw new Error("Unable to fetch wishlist.");
        }

        const cars = await response.json();

        grid.innerHTML = "";

        if (!cars.length) {
            grid.innerHTML = "<p>Your wishlist is empty.</p>";
            return;
        }

        cars.forEach(car => {
            grid.appendChild(createWishlistCard(car));
        });

        if (window.CARZO_WISHLIST) {
            window.CARZO_WISHLIST.syncHearts();
        }

    } catch (error) {
        console.error(error);
        message.textContent = "Unable to load your wishlist. Please try again.";
        grid.innerHTML = "";
    }
}

function createWishlistCard(car) {
    const card = document.createElement("article");
    card.className = "car-card";
    card.dataset.carId = car.carId;

    const imageBox = document.createElement("div");
    imageBox.className = "car-image";

    const image = document.createElement("img");
    image.src = getCarImagePath(car.imageUrl);
    image.alt = car.carName || "Car";

    image.onerror = () => {
        image.style.visibility = "hidden";
    };

    const wishlistButton = document.createElement("button");
    wishlistButton.type = "button";
    wishlistButton.className = "wishlist-button";
    wishlistButton.setAttribute("aria-label", "Remove from wishlist");

    wishlistButton.innerHTML =
        '<img src="heart-svgrepo-com.svg" alt="Wishlist" class="heart-outline">' +
        '<img src="heart-fill-svgrepo-com.svg" alt="Wishlisted" class="heart-filled">';

    imageBox.append(image, wishlistButton);

    const details = document.createElement("div");
    details.className = "car-details";

    const name = document.createElement("h3");
    name.textContent = car.carName || "Unnamed car";

    const price = document.createElement("p");
    price.textContent = formatWishlistPrice(car.minPrice, car.maxPrice);

    details.append(name, price);
    card.append(imageBox, details);

    card.addEventListener("click", event => {
        if (event.target.closest(".wishlist-button")) return;

        window.location.href =
            "car-details.html?id=" + encodeURIComponent(car.carId);
    });

    return card;
}

function getCarImagePath(path) {
    if (!path) return "";

    const normalized = String(path).trim().replace(/\\/g, "/");

    if (/^(https?:)?\/\//i.test(normalized) || normalized.startsWith("/")) {
        return normalized;
    }

    return normalized.toLowerCase().startsWith("cars/")
        ? normalized
        : "cars/" + normalized;
}

function formatWishlistPrice(min, max) {
    if (min == null && max == null) return "Price not available";

    if (min == null) {
        return `Up to ₹${Number(max).toFixed(2)} Lakh`;
    }

    if (max == null) {
        return `From ₹${Number(min).toFixed(2)} Lakh`;
    }

    return `₹${Number(min).toFixed(2)} - ₹${Number(max).toFixed(2)} Lakh`;
}
