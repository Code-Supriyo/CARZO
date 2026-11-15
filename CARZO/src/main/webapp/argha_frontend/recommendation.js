
document.addEventListener("DOMContentLoaded", () => {

    const contextPath = window.location.pathname.substring(
        0, window.location.pathname.indexOf("/", 1)
    );

    const form = document.getElementById("recommendationForm");
    const output = document.getElementById("recommendedCars");
    const error = document.getElementById("errorMessage");

    function showError(message) {
        error.textContent = message;
        error.hidden = false;
    }

    form.addEventListener("submit", async e => {

        e.preventDefault();

        error.hidden = true;
        output.textContent = "Loading recommendations...";

        const params = new URLSearchParams();

        const category = form.elements.category.value;
        const maxPrice = form.elements.maxPrice.value;
        const mileage = form.elements.mileage.value;

        if (category) params.set("category", category);
        if (maxPrice) params.set("maxPrice", maxPrice);
        if (mileage) params.set("mileage", mileage);

        try {
            const response = await fetch(
                contextPath + "/CarRecommendationServlet?" + params
            );

            const cars = await response.json();

            if (!response.ok) {
                throw new Error(cars.message || "Recommendation failed.");
            }

            output.replaceChildren();

            if (!cars.length) {
                output.textContent = "No matching cars found.";
                return;
            }

            cars.forEach(car => {

                const card = document.createElement("article");
                card.className = "car-card";

                const name = document.createElement("h3");
                name.textContent =
                    car.brandName + " " + car.carName;

                const price = document.createElement("p");
                price.textContent =
                    "Starting at ₹" + car.minPrice + " lakh";

                const mileage = document.createElement("p");
                mileage.textContent =
                    "Mileage: " + car.mileage + " km/l";

                const button = document.createElement("button");
                button.textContent = "Add to Compare";

                button.addEventListener("click", async () => {

                    const data = new URLSearchParams();
                    data.set("carId", car.carId);

                    const result = await fetch(
                        contextPath + "/AddToCompareServlet",
                        {
                            method: "POST",
                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded"
                            },
                            body: data.toString()
                        }
                    );

                    const json = await result.json();

                    if (!result.ok) {
                        showError(json.message || "Unable to add car.");
                    } else {
                        showError("Car added to comparison.");
                    }
                });

                card.append(name, price, mileage, button);
                output.append(card);
            });

        } catch (e) {
            showError(e.message);
            output.replaceChildren();
        }
    });
});
