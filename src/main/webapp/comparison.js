
document.addEventListener("DOMContentLoaded", function () {

    var path = window.location.pathname;
    var contextPath = path.substring(0, path.indexOf("/", 1));

    if (!contextPath || contextPath === path) {
        contextPath = "";
    }

    var errorMessage = document.getElementById("errorMessage");

    function escapeHTML(value) {
        return String(value == null ? "" : value).replace(
            /[&<>"']/g,
            function (c) {
                var entities = {
                    "&": "&amp;",
                    "<": "&lt;",
                    ">": "&gt;",
                    '"': "&quot;",
                    "'": "&#39;"
                };
                return entities[c];
            }
        );
    }

    function getCarName(car) {
        if (!car) {
            return "Not available";
        }

        var brand = String(car.brandName || "").trim();
        var name = String(car.carName || "").trim();

        if (brand && name.toLowerCase().indexOf(
            brand.toLowerCase() + " "
        ) === 0) {
            name = name.substring(brand.length).trim();
        }

        return escapeHTML((brand + " " + name).trim());
    }

    function showError(message) {
        if (errorMessage) {
            errorMessage.textContent = message;
            errorMessage.hidden = false;
        }
    }

    function createTable(headers, rows) {
        var html = '<div class="table-container"><table>';
        html += '<thead><tr>';

        headers.forEach(function (header) {
            html += '<th>' + escapeHTML(header) + '</th>';
        });

        html += '</tr></thead><tbody>';

        rows.forEach(function (row) {
            html += '<tr>';

            row.forEach(function (cell) {
                html += '<td>' + escapeHTML(cell) + '</td>';
            });

            html += '</tr>';
        });

        html += '</tbody></table></div>';

        return html;
    }

    function displaySelectedCars(cars) {
        var target = document.getElementById("selectedCars");

        if (!target) {
            return;
        }

        var html = "";

        cars.forEach(function (car) {
            html += '<div class="selected-car-card">';
            html += '<h3>' + getCarName(car) + '</h3>';
            html += '<p>₹' + escapeHTML(car.minPrice) +
                    ' - ₹' + escapeHTML(car.maxPrice) +
                    ' Lakh</p>';
            html += '</div>';
        });

        target.innerHTML = html;
    }

    function displayPriceComparison(cars) {
        var target = document.getElementById("priceComparison");

        if (!target) {
            return;
        }

        var rows = [];

        cars.forEach(function (car) {
            rows.push([
                getCarName(car),
                "₹" + car.minPrice + " Lakh",
                "₹" + car.maxPrice + " Lakh"
            ]);
        });

        target.innerHTML = createTable(
            ["Car", "Minimum Price", "Maximum Price"],
            rows
        );
    }

    function displayPerformanceComparison(cars) {
        var target = document.getElementById("performanceComparison");

        if (!target) {
            return;
        }

        var rows = [];

        cars.forEach(function (car) {
            rows.push([
                getCarName(car),
                car.mileage + " km/l",
                car.engineCapacity + " cc",
                car.transmission || "Not available",
                car.maxPower || "Not available",
                car.maxTorque || "Not available"
            ]);
        });

        target.innerHTML = createTable(
            [
                "Car",
                "Mileage",
                "Engine",
                "Transmission",
                "Max Power",
                "Max Torque"
            ],
            rows
        );
    }

    function displaySafetyComparison(cars) {
        var target = document.getElementById("safetyComparison");

        if (!target) {
            return;
        }

        var rows = [];

        cars.forEach(function (car) {
            rows.push([
                getCarName(car),
                car.airbags,
                car.seatingCapacity
            ]);
        });

        target.innerHTML = createTable(
            ["Car", "Airbags", "Seating Capacity"],
            rows
        );
    }

    function displaySpecifications(cars) {
        var target = document.getElementById("specificationComparison");

        if (!target) {
            return;
        }

        var fields = [
            ["Category", "categoryName"],
            ["Condition", "carCondition"],
            ["Minimum Price", "minPrice"],
            ["Maximum Price", "maxPrice"],
            ["Transmission", "transmission"],
            ["Engine Capacity", "engineCapacity"],
            ["Mileage", "mileage"],
            ["Maximum Power", "maxPower"],
            ["Maximum Torque", "maxTorque"],
            ["Length (mm)", "lengthMm"],
            ["Width (mm)", "widthMm"],
            ["Height (mm)", "heightMm"],
            ["Wheelbase (mm)", "wheelbaseMm"],
            ["Ground Clearance (mm)", "groundClearanceMm"],
            ["Boot Space (L)", "bootSpaceL"]
        ];

        var headers = ["Specification"];
        var rows = [];

        cars.forEach(function (car) {
            headers.push(getCarName(car));
        });

        fields.forEach(function (field) {
            var row = [field[0]];

            cars.forEach(function (car) {
                var value = car[field[1]];

                if (value === null || value === undefined || value === "") {
                    value = "Not available";
                }

                row.push(value);
            });

            rows.push(row);
        });

        target.innerHTML = createTable(headers, rows);
    }

    function displayFeatureComparison(cars, allFeatures) {
        var target = document.getElementById("featureComparison");

        if (!target) {
            return;
        }

        if (!allFeatures || allFeatures.length === 0) {
            target.innerHTML =
                '<p class="no-data">No feature information available.</p>';
            return;
        }

        var headers = ["Feature"];
        var rows = [];

        cars.forEach(function (car) {
            headers.push(getCarName(car));
        });

        allFeatures.forEach(function (feature) {
            var row = [feature];

            cars.forEach(function (car) {
                var available = car.features &&
                    car.features.indexOf(feature) !== -1;

                row.push(available ? "Available" : "Not Available");
            });

            rows.push(row);
        });

        target.innerHTML = createTable(headers, rows);
    }

    function displayBestChoices(data) {
        var target = document.getElementById("bestChoices");

        if (!target) {
            return;
        }

        var choices = [
            ["Best Price", data.bestPriceCar],
            ["Best Mileage", data.bestMileageCar],
            ["Best Engine", data.bestEngineCar],
            ["Best Safety", data.bestSafetyCar],
            ["Most Features", data.bestFeatureCar]
        ];

        var html = '<div class="winner-grid">';

        choices.forEach(function (choice) {
            html += '<div class="winner-card">';
            html += '<strong>' + escapeHTML(choice[0]) + '</strong>';
            html += '<p>' + getCarName(choice[1]) + '</p>';
            html += '</div>';
        });

        html += '</div>';

        target.innerHTML = html;
    }

    function loadComparison() {
        var xhr = new XMLHttpRequest();

        xhr.open(
            "GET",
            contextPath + "/CompareCarsServlet",
            true
        );

        xhr.setRequestHeader("Accept", "application/json");

        xhr.onreadystatechange = function () {
            if (xhr.readyState !== 4) {
                return;
            }

            try {
                if (xhr.status < 200 || xhr.status >= 300) {
                    throw new Error(
                        "Unable to load comparison results."
                    );
                }

                var data = JSON.parse(xhr.responseText);

                if (data.success === false) {
                    throw new Error(
                        data.message || "Comparison could not be loaded."
                    );
                }

                if (!data.cars || data.cars.length < 2) {
                    throw new Error(
                        "No valid comparison data found. Please select at least two cars."
                    );
                }

                var cars = data.cars;

                displaySelectedCars(cars);
                displayPriceComparison(cars);
                displayPerformanceComparison(cars);
                displaySafetyComparison(cars);
                displaySpecifications(cars);
                displayFeatureComparison(cars, data.allFeatures || []);
                displayBestChoices(data);

            } catch (error) {
                showError(error.message);
            }
        };

        xhr.send();
    }

    loadComparison();

});
