document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const carId = params.get("id");

    const confirmButton = document.getElementById("confirmButton");
    const purchaseForm = document.getElementById("purchaseForm");
    const errorMessage = document.getElementById("errorMessage");
    const visitDateInput = document.getElementById("preferredVisitDate");

    const today = new Date();
    const localDate = new Date(
        today.getTime() - today.getTimezoneOffset() * 60000
    ).toISOString().split("T")[0];

    visitDateInput.min = localDate;

    if (!carId || !/^\d+$/.test(carId) || Number(carId) <= 0) {
        errorMessage.textContent = "Invalid car selection.";
        return;
    }

    document.getElementById("carId").value = carId;

    document.getElementById("backLink").href =
        `car-details.html?id=${encodeURIComponent(carId)}`;

    function populateSelect(selectId, values, placeholder) {
        const select = document.getElementById(selectId);

        select.innerHTML = "";

        const defaultOption = document.createElement("option");
        defaultOption.value = "";
        defaultOption.textContent = placeholder;
        select.appendChild(defaultOption);

        values.forEach(value => {
            const option = document.createElement("option");
            option.value = value;
            option.textContent = value;
            select.appendChild(option);
        });

        select.disabled = values.length === 0;

        if (values.length === 0) {
            defaultOption.textContent = "No options available";
        }
    }

    try {
        const [carResponse, formResponse] = await Promise.all([
            fetch(`CarDetailsServ?id=${encodeURIComponent(carId)}`),
            fetch(`PurchaseFormServ?carId=${encodeURIComponent(carId)}`)
        ]);

        if (!carResponse.ok || !formResponse.ok) {
            throw new Error("Unable to retrieve purchase details.");
        }

        const [car, formData] = await Promise.all([
            carResponse.json(),
            formResponse.json()
        ]);

        const minPrice = Number(car?.minPrice);

        if (
            !car ||
            !car.carId ||
            car.minPrice == null ||
            !Number.isFinite(minPrice) ||
            minPrice <= 0
        ) {
            throw new Error("Car details or minimum price are unavailable.");
        }

        if (formData.error) {
            throw new Error(formData.error);
        }

        document.getElementById("carName").textContent =
            car.carName || "Car";

        document.getElementById("brandName").textContent =
            car.brandName || "";

        document.getElementById("carPrice").textContent =
            `₹${minPrice.toFixed(2)} Lakh`;

        const image = document.getElementById("carImage");

        if (car.imageUrl) {
            image.src = car.imageUrl;
        } else {
            image.style.display = "none";
        }

        image.alt = car.carName || "Selected car";

        document.getElementById("customerName").value =
            formData.name || "";

        document.getElementById("customerEmail").value =
            formData.email || "";

        document.getElementById("customerContact").value =
            formData.contactNo || "";

        document.getElementById("customerAddress").value =
            formData.address || "";

        populateSelect(
            "preferredColour",
            formData.colours || [],
            "Select a colour"
        );

        populateSelect(
            "preferredFuelType",
            formData.fuelTypes || [],
            "Select a fuel type"
        );

        if (
            !formData.name ||
            !formData.email ||
            !formData.contactNo ||
            !formData.address
        ) {
            throw new Error("Customer details are incomplete.");
        }

        if (
            !formData.colours?.length ||
            !formData.fuelTypes?.length
        ) {
            throw new Error("No colour or fuel options are available for this car.");
        }

        confirmButton.disabled = false;

    } catch (error) {
        console.error("Purchase form error:", error);

        errorMessage.textContent =
            error.message || "Unable to load purchase details. Please try again.";
    }

    purchaseForm.addEventListener("submit", event => {
        if (visitDateInput.value < localDate) {
            event.preventDefault();
            errorMessage.textContent =
                "Please select today or a future visit date.";
            return;
        }

        if (
            !document.getElementById("preferredColour").value ||
            !document.getElementById("preferredFuelType").value ||
            !document.getElementById("preferredVisitTime").value
        ) {
            event.preventDefault();
            errorMessage.textContent =
                "Please complete all your preferences and visit details.";
            return;
        }

        errorMessage.textContent = "";
        confirmButton.disabled = true;
        confirmButton.textContent = "Processing...";
    });
});
