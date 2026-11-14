<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList,com.carzo.model.Car"%>

<%
    List<Car> recommendedCars = new ArrayList<>();
    Object recommendedCarsObject = request.getAttribute("recommendedCars");

    if(recommendedCarsObject instanceof List<?>){
        for(Object object : (List<?>)recommendedCarsObject){
            if(object instanceof Car) recommendedCars.add((Car)object);
        }
    }

    String errorMessage = (String)request.getAttribute("errorMessage");
    String selectedCategory = (String)request.getAttribute("selectedCategory");
    String selectedMaxPrice = (String)request.getAttribute("selectedMaxPrice");
    String selectedMileage = (String)request.getAttribute("selectedMileage");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Car Recommendations | Carzo</title>
<link rel="stylesheet" href="<%=contextPath%>/comparison/comparison.css">
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
.page-subtitle{color:#69758a;margin-bottom:30px;line-height:1.6}
.filter-box{background:white;padding:28px;border-radius:14px;box-shadow:0 5px 22px #14234114;margin-bottom:35px}
.section-title{color:#172440;font-size:23px;margin-bottom:22px}
.filter-row{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:18px;align-items:end}
.filter-group{display:flex;flex-direction:column;gap:9px}
.filter-group label{font-size:14px;font-weight:700}
.car-select,.search-box{width:100%;padding:12px;border:1px solid #d8deea;border-radius:8px;background:white;font-size:14px}
.btn{display:inline-block;padding:12px 20px;border:0;border-radius:8px;text-decoration:none;text-align:center;font-size:14px;font-weight:700;cursor:pointer}
.btn-primary{background:#ed3545;color:white}
.btn-secondary{background:#263c63;color:white}
.car-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:24px;margin-top:20px}
.car-card{background:white;border-radius:14px;overflow:hidden;box-shadow:0 5px 20px #14234117;transition:.3s}
.car-card:hover{transform:translateY(-5px)}
.car-image{width:100%;height:205px;object-fit:cover;background:#e9edf4}
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
.car-content form .btn{width:100%}
.error-message{background:#fff0f0;color:#c62828;border-left:4px solid #d83242;padding:14px 18px;border-radius:7px;margin-bottom:22px}
.no-data{grid-column:1/-1;background:white;padding:35px;text-align:center;border-radius:12px;color:#69758a}
.button-container{display:flex;justify-content:center;gap:15px;margin:35px 0}
@media(max-width:950px){.filter-row{grid-template-columns:repeat(2,minmax(0,1fr))}.car-grid{grid-template-columns:repeat(2,minmax(0,1fr))}}
@media(max-width:600px){.carzo-header{flex-direction:column;gap:15px}.carzo-nav{gap:15px;flex-wrap:wrap;justify-content:center}.comparison-container{width:92%}.filter-row,.car-grid{grid-template-columns:1fr}.button-container{flex-direction:column}}
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
<h1 class="page-title">Car Recommendations</h1>
<p class="page-subtitle">Find cars according to your preferred category, budget and mileage.</p>

<%if(errorMessage!=null&&!errorMessage.trim().isEmpty()){%>
<div class="error-message"><%=errorMessage%></div>
<%}%>

<section class="filter-box">
<h2 class="section-title">Find Your Suitable Car</h2>

<form action="<%=contextPath%>/CarRecommendationServlet" method="get">
<div class="filter-row">

<div class="filter-group">
<label for="category">Car Category</label>
<select name="category" id="category" class="car-select">
<option value="">All Categories</option>
<option value="1" <%= "1".equals(selectedCategory)?"selected":"" %>>SUV</option>
<option value="2" <%= "2".equals(selectedCategory)?"selected":"" %>>Sedan</option>
<option value="3" <%= "3".equals(selectedCategory)?"selected":"" %>>Hatchback</option>
<option value="4" <%= "4".equals(selectedCategory)?"selected":"" %>>MUV</option>
<option value="5" <%= "5".equals(selectedCategory)?"selected":"" %>>Coupe</option>
<option value="6" <%= "6".equals(selectedCategory)?"selected":"" %>>Convertible</option>
</select>
</div>

<div class="filter-group">
<label for="maxPrice">Maximum Budget (Lakhs)</label>
<input type="number" name="maxPrice" id="maxPrice" class="search-box" min="0" step="0.01" placeholder="Enter maximum budget" value="<%=selectedMaxPrice!=null?selectedMaxPrice:""%>">
</div>

<div class="filter-group">
<label for="mileage">Minimum Mileage (km/l)</label>
<input type="number" name="mileage" id="mileage" class="search-box" min="0" step="0.1" placeholder="Enter minimum mileage" value="<%=selectedMileage!=null?selectedMileage:""%>">
</div>

<div class="filter-group">
<button type="submit" class="btn btn-primary">Find Cars</button>
</div>

</div>
</form>
</section>

<h2 class="section-title">Recommended Cars</h2>

<div class="car-grid">
<%if(recommendedCars.isEmpty()){%>
<div class="no-data">No matching cars found. Try changing your filters.</div>
<%}else{for(Car car:recommendedCars){
    String imageUrl=car.getImageUrl();
    if(imageUrl!=null){
        imageUrl=imageUrl.trim();
        if(imageUrl.startsWith("/")) imageUrl=imageUrl.substring(1);
    }
%>

<div class="car-card">
<%if(imageUrl!=null&&!imageUrl.isEmpty()){%>
<img src="<%=contextPath%>/<%=imageUrl%>" alt="<%=car.getCarName()%>" class="car-image" onerror="this.style.display='none';">
<%}%>

<div class="car-content">
<p class="brand-name"><%=car.getBrandName()!=null?car.getBrandName():""%></p>
<h3 class="car-name"><%=car.getCarName()%></h3>

<p class="car-price">₹<%=car.getMinPrice()%> - ₹<%=car.getMaxPrice()%> Lakh</p>

<span class="category-badge"><%=car.getCategoryName()!=null?car.getCategoryName():"Not Available"%></span>

<div class="spec-grid">
<div class="spec-item"><strong>Mileage</strong><span><%=car.getMileage()%> km/l</span></div>
<div class="spec-item"><strong>Engine</strong><span><%=car.getEngineCapacity()%> cc</span></div>
<div class="spec-item"><strong>Seats</strong><span><%=car.getSeatingCapacity()%></span></div>
<div class="spec-item"><strong>Airbags</strong><span><%=car.getAirbags()%></span></div>
<div class="spec-item"><strong>Transmission</strong><span><%=car.getTransmission()!=null?car.getTransmission():"Not Available"%></span></div>
<div class="spec-item"><strong>Condition</strong><span><%=car.getCarCondition()!=null?car.getCarCondition():"Not Available"%></span></div>
<div class="spec-item"><strong>Launch Status</strong><span><%=car.getLaunchStatus()!=null?car.getLaunchStatus():"Not Available"%></span></div>
</div>

<p class="description"><%=car.getDescription()!=null?car.getDescription():""%></p>

<form action="<%=contextPath%>/AddToCompareServlet" method="post">
<input type="hidden" name="carId" value="<%=car.getCarId()%>">
<button type="submit" class="btn btn-primary">Add to Compare</button>
</form>

</div>
</div>

<%}}%>
</div>

<div class="button-container">
<a href="<%=contextPath%>/CarSelectionServlet" class="btn btn-secondary">Browse All Cars</a>
<a href="<%=contextPath%>/CompareCarsServlet" class="btn btn-primary">Compare Selected Cars</a>
</div>
</main>
</body>
</html>
