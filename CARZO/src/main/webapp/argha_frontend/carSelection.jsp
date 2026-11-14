<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList,com.carzo.model.Car"%>

<%
    String contextPath = request.getContextPath();
    List<Car> carList = new ArrayList<>();

    Object carListObject = request.getAttribute("carList");

    if (carListObject instanceof List<?>) {
        for (Object object : (List<?>) carListObject) {
            if (object instanceof Car) {
                carList.add((Car) object);
            }
        }
    }

    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Car Selection | Carzo</title>
<link rel="stylesheet" href="<%=contextPath%>/css/comparison.css">

<style>
*{box-sizing:border-box}
body{margin:0;background:#f4f6fb;font-family:Arial,sans-serif;color:#202a3b}
.carzo-header{background:linear-gradient(110deg,#101b35,#263c63);color:white;padding:18px 7%;display:flex;justify-content:space-between;align-items:center}
.carzo-logo{font-size:30px;font-weight:800;color:#ff424f}
.carzo-nav{display:flex;gap:28px}
.carzo-nav a{color:white;text-decoration:none;font-weight:600}
.carzo-nav a:hover{color:#ff5964}
.comparison-container{width:86%;max-width:1250px;margin:35px auto}
.page-title{font-size:34px;color:#172440;margin-bottom:8px}
.page-subtitle{color:#69758a;line-height:1.6;margin-bottom:28px}
.filter-box{background:white;padding:25px;border-radius:14px;box-shadow:0 5px 22px #14234114;margin-bottom:25px}
.filter-row{display:grid;grid-template-columns:2fr 1fr auto;gap:18px;align-items:end}
.filter-group{display:flex;flex-direction:column;gap:9px}
.filter-group label{font-size:14px;font-weight:700}
.search-box,.car-select{width:100%;padding:12px;border:1px solid #d8deea;border-radius:8px;font-size:14px}
.btn{display:inline-block;padding:12px 21px;border:0;border-radius:8px;text-decoration:none;text-align:center;font-weight:700;cursor:pointer}
.btn-primary{background:#ed3545;color:white}
.btn-secondary{background:#263c63;color:white}
.btn:disabled{background:#aeb5c2;cursor:not-allowed}
.button-container{display:flex;align-items:center;justify-content:space-between;gap:15px;margin:25px 0;flex-wrap:wrap}
.selected-count{color:#263c63;font-weight:700;background:white;padding:12px 20px;border-radius:8px}
#selectedCount{color:#ed3545;font-size:20px}
.car-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:24px}
.car-card{background:white;border-radius:14px;overflow:hidden;box-shadow:0 5px 20px #14234117;transition:.3s;border:2px solid transparent}
.car-card:hover{transform:translateY(-5px);box-shadow:0 10px 28px #14234126}
.image-container{height:205px;background:#e9edf4;overflow:hidden}
.car-image{width:100%;height:100%;object-fit:cover}
.car-content{padding:20px}
.brand-name{color:#ed3545;font-size:13px;font-weight:700;text-transform:uppercase}
.car-name{color:#172440;font-size:22px;margin:8px 0 12px}
.car-price{color:#263c63;font-size:17px;font-weight:800}
.category-badge{display:inline-block;background:#fff0f1;color:#d82e3e;padding:6px 12px;border-radius:20px;font-size:12px;font-weight:700;margin:10px 0}
.spec-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:12px;padding:15px 0;border-top:1px solid #edf0f5;border-bottom:1px solid #edf0f5}
.spec-item{display:flex;flex-direction:column;gap:5px;font-size:13px}
.spec-item strong{color:#69758a}
.spec-item span{font-weight:700}
.description{color:#69758a;font-size:13px;line-height:1.6;min-height:40px;margin:15px 0}
.compare-select{display:flex;align-items:center;gap:10px;padding:12px;background:#f4f6fb;border:1px solid #e1e6ef;border-radius:8px;font-weight:700;cursor:pointer}
.compare-select input{width:18px;height:18px;accent-color:#ed3545}
.error-message{background:#fff0f0;color:#c62828;border-left:4px solid #d83242;padding:14px 18px;border-radius:7px;margin-bottom:22px}
.no-data{grid-column:1/-1;background:white;padding:35px;text-align:center;border-radius:12px;color:#69758a}
@media(max-width:950px){.car-grid{grid-template-columns:repeat(2,minmax(0,1fr))}.filter-row{grid-template-columns:1fr 1fr}}
@media(max-width:600px){.carzo-header{flex-direction:column;gap:15px}.carzo-nav{gap:15px;flex-wrap:wrap;justify-content:center}.comparison-container{width:92%}.car-grid,.filter-row{grid-template-columns:1fr}.button-container{flex-direction:column;align-items:stretch}.selected-count{text-align:center}}
</style>
</head>

<body>

<header class="carzo-header">
    <div class="carzo-logo">Carzo</div>
    <nav class="carzo-nav">
        <a href="<%=contextPath%>/CarSelectionServlet">Cars</a>
        <a href="<%=contextPath%>/CarRecommendationServlet">Recommendations</a>
        <a href="<%=contextPath%>/CompareCarsServlet">Compare</a>
    </nav>
</header>

<main class="comparison-container">

<h1 class="page-title">Select Cars to Compare</h1>
<p class="page-subtitle">
Select between 2 and 4 cars to compare their price, performance, features and specifications.
</p>

<% if(errorMessage!=null && !errorMessage.trim().isEmpty()){ %>
<div class="error-message"><%=errorMessage%></div>
<% } %>

<section class="filter-box">
<form action="<%=contextPath%>/CarSelectionServlet" method="get">
<div class="filter-row">

<div class="filter-group">
<label for="carSearch">Search Car</label>
<input type="text" id="carSearch" name="search" class="search-box" placeholder="Search car or brand">
</div>

<div class="filter-group">
<label for="category">Category</label>
<select id="category" name="category" class="car-select">
<option value="">All Categories</option>
<option value="1">SUV</option>
<option value="2">Sedan</option>
<option value="3">Hatchback</option>
<option value="4">MUV</option>
<option value="5">Coupe</option>
<option value="6">Convertible</option>
</select>
</div>

<div class="filter-group">
<button type="submit" class="btn btn-primary">Search</button>
</div>

</div>
</form>
</section>

<div class="button-container">
<button type="button" id="selectAllButton" class="btn btn-secondary">Select First 4</button>
<button type="button" id="clearSelectionButton" class="btn btn-secondary">Clear Selection</button>
<div class="selected-count">Selected Cars: <span id="selectedCount">0</span> / 4</div>
</div>

<form action="<%=contextPath%>/CompareCarsServlet" method="post" id="compareForm">

<div class="car-grid">

<% if(carList.isEmpty()){ %>
<div class="no-data">No cars found.</div>
<% } else { 
    for(Car car : carList){
        String imageUrl = car.getImageUrl();
        if(imageUrl != null){
            imageUrl = imageUrl.trim();
            if(imageUrl.startsWith("/")) imageUrl = imageUrl.substring(1);
        }
%>

<div class="car-card">

<div class="image-container">
<% if(imageUrl != null && !imageUrl.isEmpty()){ %>
<img src="<%=contextPath%>/<%=imageUrl%>"
     alt="<%=car.getCarName()%>"
     class="car-image"
     onerror="this.style.display='none';">
<% } %>
</div>

<div class="car-content">
<p class="brand-name"><%=car.getBrandName()!=null?car.getBrandName():""%></p>
<h3 class="car-name"><%=car.getCarName()%></h3>

<p class="car-price">
₹<%=car.getMinPrice()%> - ₹<%=car.getMaxPrice()%> Lakh
</p>

<span class="category-badge">
<%=car.getCategoryName()!=null?car.getCategoryName():"Not Available"%>
</span>

<div class="spec-grid">
<div class="spec-item"><strong>Mileage</strong><span><%=car.getMileage()%> km/l</span></div>
<div class="spec-item"><strong>Engine</strong><span><%=car.getEngineCapacity()%> cc</span></div>
<div class="spec-item"><strong>Seats</strong><span><%=car.getSeatingCapacity()%></span></div>
<div class="spec-item"><strong>Airbags</strong><span><%=car.getAirbags()%></span></div>
<div class="spec-item"><strong>Transmission</strong><span><%=car.getTransmission()!=null?car.getTransmission():"Not Available"%></span></div>
<div class="spec-item"><strong>Condition</strong><span><%=car.getCarCondition()!=null?car.getCarCondition():"Not Available"%></span></div>
</div>

<p class="description"><%=car.getDescription()!=null?car.getDescription():""%></p>

<label class="compare-select">
<input type="checkbox" name="carIds" value="<%=car.getCarId()%>">
<span>Select for Comparison</span>
</label>

</div>
</div>

<% } } %>
</div>

<div class="button-container">
<button type="submit" id="compareButton" class="btn btn-primary" disabled>
Compare Selected Cars
</button>
</div>

</form>
</main>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const checkboxes = document.querySelectorAll('input[name="carIds"]');
    const countDisplay = document.getElementById("selectedCount");
    const compareButton = document.getElementById("compareButton");
    const selectAllButton = document.getElementById("selectAllButton");
    const clearButton = document.getElementById("clearSelectionButton");

    function updateSelection() {
        const selected = document.querySelectorAll('input[name="carIds"]:checked').length;
        countDisplay.textContent = selected;
        compareButton.disabled = selected < 2 || selected > 4;
        checkboxes.forEach(function (checkbox) {
            checkbox.disabled = !checkbox.checked && selected >= 4;
        });
    }

    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener("change", updateSelection);
    });

    selectAllButton.addEventListener("click", function () {
        let count = 0;
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = count < 4;
            count++;
        });
        updateSelection();
    });

    clearButton.addEventListener("click", function () {
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = false;
            checkbox.disabled = false;
        });
        updateSelection();
    });

    updateSelection();
});
</script>

</body>
</html>
