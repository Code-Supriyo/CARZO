
document.addEventListener("DOMContentLoaded", async () => {

    const contextPath = window.location.pathname.substring(
        0, window.location.pathname.indexOf("/", 1)
    );

    const error = document.getElementById("errorMessage");

    function showError(message) {
        if (error) {
            error.textContent = message;
            error.hidden = false;
        }
    }

    function table(headers, rows) {

        const wrapper = document.createElement("div");
        wrapper.className = "table-container";

        const table = document.createElement("table");

        const thead = document.createElement("thead");
        const headRow = document.createElement("tr");

        headers.forEach(header => {
            const th = document.createElement("th");
            th.textContent = header;
            headRow.append(th);
        });

        thead.append(headRow);

        const tbody = document.createElement("tbody");

        rows.forEach(row => {
            const tr = document.createElement("tr");

            row.forEach(value => {
                const td = document.createElement("td");
                td.textContent = value == null ? "-" : value;
                tr.append(td);
            });

            tbody.append(tr);
        });

        table.append(thead, tbody);
        wrapper.append(table);

        return wrapper;
    }

    function formatPrice(value) {
        return "₹" + Number(value).toFixed(2) + " lakh";
    }

    function showCars(cars, container) {

        if (!container) return;

        container.replaceChildren();

        cars.forEach(car => {

            const card = document.createElement("article");
            card.className = "car-card";

            const name = document.createElement("h3");
            name.textContent =
                car.brandName + " " + car.carName;

            const price = document.createElement("p");
            price.textContent =
                formatPrice(car.minPrice) + " - " +
                formatPrice(car.maxPrice);

            card.append(name, price);
            container.append(card);
        });
    }

    function renderPrice(cars, container) {
        if (!container) return;

        container.replaceChildren();

        container.append(table(
            ["Car", "Minimum Price", "Maximum Price"],
            cars.map(c => [
                c.brandName + " " + c.carName,
                formatPrice(c.minPrice),
                formatPrice(c.maxPrice)
            ])
        ));
    }

    function renderPerformance(cars, container) {
        if (!container) return;

        container.replaceChildren();

        container.append(table(
            ["Car", "Mileage", "Engine", "Power", "Torque"],
            cars.map(c => [
                c.brandName + " " + c.carName,
                c.mileage + " km/l",
                c.engineCapacity + " cc",
                c.maxPower,
                c.maxTorque
            ])
        ));
    }

    function renderSpecifications(cars, container) {
        if (!container) return;

        container.replaceChildren();

        container.append(table(
            ["Car", "Length", "Width", "Height", "Wheelbase",
             "Ground Clearance", "Boot Space"],
            cars.map(c => [
                c.carName,
                c.lengthMm + " mm",
                c.widthMm + " mm",
                c.heightMm + " mm",
                c.wheelbaseMm + " mm",
                c.groundClearanceMm + " mm",
                c.bootSpaceL + " L"
            ])
        ));
    }

    function renderSafety(cars, container) {
        if (!container) return;

        container.replaceChildren();

        container.append(table(
            ["Car", "Airbags", "Seats"],
            cars.map(c => [
                c.brandName + " " + c.carName,
                c.airbags,
                c.seatingCapacity
            ])
        ));
    }

    function renderFeatures(cars, container, allFeatures) {
        if (!container) return;

        container.replaceChildren();

        container.append(table(
            ["Feature", ...cars.map(c => c.carName)],
            allFeatures.map(feature => [
                feature,
                ...cars.map(c =>
                    c.features.includes(feature) ? "Available" : "Not Available"
                )
            ])
        ));
    }

    function renderWinners(data, container) {
        if (!container) return;

        container.replaceChildren();

        const winners = [
            ["Best Price", data.bestPriceCar],
            ["Best Mileage", data.bestMileageCar],
            ["Best Engine", data.bestEngineCar],
            ["Best Safety", data.bestSafetyCar],
            ["Most Features", data.bestFeatureCar]
        ];

        winners.forEach(([title, car]) => {

            if (!car) return;

            const card = document.createElement("article");
            card.className = "winner-card";

            const heading = document.createElement("h3");
            heading.textContent = title;

            const name = document.createElement("p");
            name.textContent =
                car.brandName + " " + car.carName;

            card.append(heading, name);
            container.append(card);
        });
    }

    try {

        const response = await fetch(
            contextPath + "/CompareCarsServlet"
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "No comparison available.");
        }

        const cars = data.cars || [];

        const page = window.location.pathname.split("/").pop();

        showCars(
            cars,
            document.getElementById("selectedCars") ||
            document.getElementById("comparisonContainer")
        );

        renderPrice(
            cars,
            document.getElementById("priceComparison") ||
            document.getElementById("comparisonSummary")
        );

        renderPerformance(
            cars,
            document.getElementById("performanceComparison")
        );

        renderSafety(
            cars,
            document.getElementById("safetyComparison")
        );

        renderSpecifications(
            cars,
            document.getElementById("specificationComparison")
        );

        renderFeatures(
            cars,
            document.getElementById("featureComparison"),
            data.allFeatures || []
        );

        renderWinners(
            data,
            document.getElementById("bestChoices") ||
            document.getElementById("comparisonWinners")
        );

    } catch (e) {
        showError(e.message);
    }
});
