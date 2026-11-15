
(() => {
    const endpoint = "WishlistServ";
    let wishlistIds = new Set();
    let loaded = false;

    async function loadIds() {
        try {
            const response = await fetch(`${endpoint}?action=ids`, {
                cache: "no-store"
            });

            if (!response.ok) throw new Error("Unable to load wishlist IDs.");

            const ids = await response.json();
            wishlistIds = new Set(ids.map(Number));
            loaded = true;
            syncHearts();

        } catch (error) {
            console.error("Wishlist loading error:", error);
        }
    }

    function getCarId(button) {
        const card = button.closest("[data-car-id]");

        if (card) return Number(card.dataset.carId);

        if (button.dataset.carId) {
            return Number(button.dataset.carId);
        }

        return null;
    }

    function updateButton(button, liked) {
        button.classList.toggle("liked", liked);
        button.setAttribute(
            "aria-label",
            liked ? "Remove from wishlist" : "Add to wishlist"
        );
    }

    function syncHearts() {
        if (!loaded) return;

        document.querySelectorAll(".wishlist-button, .wishlist-btn")
            .forEach(button => {
                const carId = getCarId(button);

                if (carId) {
                    updateButton(button, wishlistIds.has(carId));
                }
            });
    }

    async function updateWishlist(action, carId) {
        const response = await fetch(`${endpoint}?action=${action}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ carId })
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || "Wishlist update failed.");
        }

        return result;
    }

    document.addEventListener("click", async event => {
        const button = event.target.closest(".wishlist-button, .wishlist-btn");

        if (!button) return;

        event.preventDefault();
        event.stopPropagation();

        const carId = getCarId(button);

        if (!carId) {
            console.error("Car ID not found for wishlist button.");
            return;
        }

        if (button.dataset.processing === "true") return;

        button.dataset.processing = "true";

        const wasLiked = wishlistIds.has(carId);
        const action = wasLiked ? "remove" : "add";

        try {
            await updateWishlist(action, carId);

            if (wasLiked) {
                wishlistIds.delete(carId);

                const card = button.closest(".car-card");

                if (document.body.classList.contains("wishlist-page") && card) {
                    card.remove();
                    updateEmptyMessage();
                }
            } else {
                wishlistIds.add(carId);
            }

            syncHearts();

        } catch (error) {
            console.error("Wishlist update error:", error);
            alert("Unable to update your wishlist. Please try again.");
        } finally {
            delete button.dataset.processing;
        }
    });

    function updateEmptyMessage() {
        const grid = document.getElementById("wishlistGrid");
        if (!grid) return;

        if (!grid.querySelector(".car-card")) {
            grid.innerHTML = "<p>Your wishlist is empty.</p>";
        }
    }

    const observer = new MutationObserver(() => {
        syncHearts();
    });

    document.addEventListener("DOMContentLoaded", () => {
        loadIds();

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    });

    window.CARZO_WISHLIST = {
        syncHearts,
        loadIds,
        getIds: () => Array.from(wishlistIds)
    };
})();
