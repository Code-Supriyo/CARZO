<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Set,java.util.LinkedHashSet,com.carzo.model.Car,com.carzo.model.ComparisonResult"%>

<%
    String contextPath=request.getContextPath();
    ComparisonResult result=null;

    Object requestResult=request.getAttribute("comparisonResult");
    if(requestResult instanceof ComparisonResult){
        result=(ComparisonResult)requestResult;
    }else{
        Object sessionResult=session.getAttribute("comparisonResult");
        if(sessionResult instanceof ComparisonResult){
            result=(ComparisonResult)sessionResult;
        }
    }

    List<Car> cars=new ArrayList<>();
    if(result!=null&&result.getCars()!=null){
        cars=result.getCars();
    }

    Set<String> allFeatures=new LinkedHashSet<>();
    for(Car car:cars){
        if(car.getFeatures()!=null){
            for(String feature:car.getFeatures()){
                if(feature!=null&&!feature.trim().isEmpty()){
                    allFeatures.add(feature.trim());
                }
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Comparison Result | Carzo</title>
<link rel="stylesheet" href="<%=contextPath%>/comparison/comparison.css">
<style>
*{box-sizing:border-box}
body{margin:0;font-family:Arial,sans-serif;background:#f4f6fb;color:#202b3c}
.carzo-header{background:#101c35;padding:15px 7%;display:flex;justify-content:space-between;align-items:center}
.carzo-logo a{color:white;font-size:30px;font-weight:800;text-decoration:none}
.carzo-nav{display:flex;gap:28px}
.carzo-nav a{color:white;text-decoration:none;font-weight:600}
.carzo-nav a:hover{color:#ff4b55}
.comparison-container{width:90%;max-width:1400px;margin:35px auto;background:white;padding:35px;border-radius:14px;box-shadow:0 5px 25px #00000012}
.page-title{text-align:center;color:#101c35;font-size:34px}
.page-subtitle{text-align:center;color:#718096;margin-bottom:35px}
.section-title{color:#101c35;font-size:23px;margin:38px 0 20px;padding-left:14px;border-left:5px solid #e63946}
.car-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:22px}
.car-card{background:white;border:1px solid #e5eaf1;border-radius:12px;overflow:hidden;box-shadow:0 4px 14px #0000000f}
.car-image{height:180px;background:#eef1f6;display:flex;align-items:center;justify-content:center}
.car-image img{width:100%;height:100%;object-fit:contain}
.car-content{padding:20px}
.brand-name{color:#e63946;font-weight:700;font-size:13px;text-transform:uppercase}
.car-name{color:#101c35;font-size:21px}
.car-price{color:#e63946;font-weight:700;font-size:17px}
.spec-grid{display:grid;grid-template-columns:repeat(2,1fr);gap:12px}
.spec-item{background:#f4f6fb;padding:11px;border-radius:7px;display:flex;flex-direction:column;gap:5px}
.spec-item strong{color:#65748b;font-size:12px}
.spec-item span{color:#17243b;font-size:14px;font-weight:600}
.comparison-table{width:100%;overflow-x:auto;border:1px solid #e5eaf1;border-radius:10px;margin-bottom:25px}
table{width:100%;min-width:550px;border-collapse:collapse;background:white}
th,td{padding:15px 17px;text-align:center;border-bottom:1px solid #e8edf3}
th{background:#101c35;color:white}
td{color:#354258}
td:first-child{background:#f4f6fb;color:#101c35;font-weight:700;text-align:left}
.feature-available{color:#16834a;font-weight:700}
.feature-unavailable{color:#d83b45;font-weight:700}
.recommendation-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(210px,1fr));gap:18px}
.recommendation-card{background:#f8faff;border:1px solid #e3e9f2;border-top:4px solid #e63946;padding:20px;border-radius:10px}
.recommendation-card h3{color:#e63946;margin-top:0}
.recommendation-card p{color:#526176;line-height:1.6}
.button-container{display:flex;justify-content:center;flex-wrap:wrap;gap:13px;margin-top:40px}
.btn{display:inline-block;padding:12px 21px;border:0;border-radius:7px;text-decoration:none;font-size:14px;font-weight:700;cursor:pointer}
.btn-primary{background:#e63946;color:white}
.btn-secondary{background:#101c35;color:white}
.btn-dark{background:#39465b;color:white}
.error-message{background:#fff0f0;color:#c62828;border:1px solid #ffcaca;padding:18px;text-align:center;border-radius:8px;font-weight:600}
@media(max-width:700px){.carzo-header{flex-direction:column;gap:15px}.carzo-nav{gap:20px}.comparison-container{width:95%;padding:20px 13px}.page-title{font-size:27px}.button-container{flex-direction:column}}
@media print{.carzo-header,.button-container{display:none}.comparison-container{width:100%;margin:0;padding:10px;box-shadow:none}}
</style>
</head>

<body>
<header class="carzo-header">
<div class="carzo-logo"><a href="<%=contextPath%>/CarSelectionServlet">Carzo</a></div>
<nav class="carzo-nav">
<a href="<%=contextPath%>/CarSelectionServlet">Cars</a>
<a href="<%=contextPath%>/CarSelectionServlet">Compare</a>
</nav>
</header>

<main class="comparison-container">
<h1 class="page-title">Car Comparison Result</h1>
<p class="page-subtitle">Complete comparison of your selected cars.</p>

<%if(cars.size()<2){%>
<div class="error-message">No comparison result is available.</div>
<div class="button-container">
<a class="btn btn-primary" href="<%=contextPath%>/CarSelectionServlet">Select Cars</a>
</div>
<%}else{%>

<h2 class="section-title">Selected Cars</h2>
<div class="car-grid">

<%for(Car car:cars){
String imageUrl=car.getImageUrl();
if(imageUrl!=null){
imageUrl=imageUrl.trim();
if(imageUrl.startsWith("/"))imageUrl=imageUrl.substring(1);
}
%>

<div class="car-card">
<div class="car-image">
<%if(imageUrl!=null&&!imageUrl.isEmpty()){%>
<img src="<%=contextPath%>/<%=imageUrl%>" alt="<%=car.getCarName()%>" onerror="this.style.display='none';">
<%}%>
</div>
<div class="car-content">
<div class="brand-name"><%=car.getBrandName()!=null?car.getBrandName():""%></div>
<h3 class="car-name"><%=car.getCarName()%></h3>
<div class="car-price">₹<%=String.format("%.2f",car.getMinPrice())%> - ₹<%=String.format("%.2f",car.getMaxPrice())%> Lakh</div>

<div class="spec-grid">
<div class="spec-item"><strong>Category</strong><span><%=car.getCategoryName()!=null?car.getCategoryName():"Not Available"%></span></div>
<div class="spec-item"><strong>Mileage</strong><span><%=car.getMileage()%> km/l</span></div>
<div class="spec-item"><strong>Engine</strong><span><%=car.getEngineCapacity()%> cc</span></div>
<div class="spec-item"><strong>Seats</strong><span><%=car.getSeatingCapacity()%></span></div>
<div class="spec-item"><strong>Airbags</strong><span><%=car.getAirbags()%></span></div>
<div class="spec-item"><strong>Transmission</strong><span><%=car.getTransmission()!=null?car.getTransmission():"Not Available"%></span></div>
</div>
</div>
</div>
<%}%>
</div>

<h2 class="section-title">Price Comparison</h2>
<div class="comparison-table">
<table>
<tr><th>Price</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<tr><td>Minimum Price</td><%for(Car car:cars){%><td>₹<%=String.format("%.2f",car.getMinPrice())%> Lakh</td><%}%></tr>
<tr><td>Maximum Price</td><%for(Car car:cars){%><td>₹<%=String.format("%.2f",car.getMaxPrice())%> Lakh</td><%}%></tr>
</table>
</div>

<h2 class="section-title">Performance Comparison</h2>
<div class="comparison-table">
<table>
<tr><th>Performance</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<tr><td>Mileage</td><%for(Car car:cars){%><td><%=car.getMileage()%> km/l</td><%}%></tr>
<tr><td>Engine Capacity</td><%for(Car car:cars){%><td><%=car.getEngineCapacity()%> cc</td><%}%></tr>
<tr><td>Maximum Power</td><%for(Car car:cars){%><td><%=car.getMaxPower()!=null?car.getMaxPower():"Not Available"%></td><%}%></tr>
<tr><td>Maximum Torque</td><%for(Car car:cars){%><td><%=car.getMaxTorque()!=null?car.getMaxTorque():"Not Available"%></td><%}%></tr>
<tr><td>Transmission</td><%for(Car car:cars){%><td><%=car.getTransmission()!=null?car.getTransmission():"Not Available"%></td><%}%></tr>
</table>
</div>

<h2 class="section-title">Safety Comparison</h2>
<div class="comparison-table">
<table>
<tr><th>Safety</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<tr><td>Airbags</td><%for(Car car:cars){%><td><%=car.getAirbags()%></td><%}%></tr>
<tr><td>Seating Capacity</td><%for(Car car:cars){%><td><%=car.getSeatingCapacity()%> Seats</td><%}%></tr>
</table>
</div>

<h2 class="section-title">Technical Specifications</h2>
<div class="comparison-table">
<table>
<tr><th>Specification</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<tr><td>Length</td><%for(Car car:cars){%><td><%=car.getLengthMm()%> mm</td><%}%></tr>
<tr><td>Width</td><%for(Car car:cars){%><td><%=car.getWidthMm()%> mm</td><%}%></tr>
<tr><td>Height</td><%for(Car car:cars){%><td><%=car.getHeightMm()%> mm</td><%}%></tr>
<tr><td>Wheelbase</td><%for(Car car:cars){%><td><%=car.getWheelbaseMm()%> mm</td><%}%></tr>
<tr><td>Ground Clearance</td><%for(Car car:cars){%><td><%=car.getGroundClearanceMm()%> mm</td><%}%></tr>
<tr><td>Boot Space</td><%for(Car car:cars){%><td><%=car.getBootSpaceL()%> L</td><%}%></tr>
</table>
</div>

<h2 class="section-title">Feature Comparison</h2>
<div class="comparison-table">
<table>
<tr><th>Feature</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<%if(allFeatures.isEmpty()){%>
<tr><td colspan="<%=cars.size()+1%>">No feature data available.</td></tr>
<%}else{for(String feature:allFeatures){%>
<tr><td><%=feature%></td>
<%for(Car car:cars){
boolean available=false;
if(car.getFeatures()!=null){
for(String carFeature:car.getFeatures()){
if(carFeature!=null&&carFeature.trim().equalsIgnoreCase(feature)){available=true;break;}
}
}
%>
<td><%if(available){%><span class="feature-available">✓ Available</span><%}else{%><span class="feature-unavailable">✗ Not Available</span><%}%></td>
<%}%>
</tr>
<%}}%>
</table>
</div>

<h2 class="section-title">Best Choices</h2>
<div class="recommendation-grid">

<%if(result.getBestPriceCar()!=null){%>
<div class="recommendation-card"><h3>Best Price</h3><p><strong><%=result.getBestPriceCar().getCarName()%></strong></p><p>Starting from ₹<%=String.format("%.2f",result.getBestPriceCar().getMinPrice())%> Lakh</p></div>
<%}if(result.getBestMileageCar()!=null){%>
<div class="recommendation-card"><h3>Best Mileage</h3><p><strong><%=result.getBestMileageCar().getCarName()%></strong></p><p>Mileage: <%=result.getBestMileageCar().getMileage()%> km/l</p></div>
<%}if(result.getBestEngineCar()!=null){%>
<div class="recommendation-card"><h3>Best Engine</h3><p><strong><%=result.getBestEngineCar().getCarName()%></strong></p><p>Engine: <%=result.getBestEngineCar().getEngineCapacity()%> cc</p></div>
<%}if(result.getBestSafetyCar()!=null){%>
<div class="recommendation-card"><h3>Best Safety</h3><p><strong><%=result.getBestSafetyCar().getCarName()%></strong></p><p>Airbags: <%=result.getBestSafetyCar().getAirbags()%></p></div>
<%}if(result.getBestFeatureCar()!=null){%>
<div class="recommendation-card"><h3>Best Features</h3><p><strong><%=result.getBestFeatureCar().getCarName()%></strong></p><p>Features: <%=result.getBestFeatureCar().getFeatures()!=null?result.getBestFeatureCar().getFeatures().size():0%></p></div>
<%}%>
</div>

<div class="button-container">
<a class="btn btn-primary" href="<%=contextPath%>/comparison/priceComparison.jsp">Price Comparison</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/performanceComparison.jsp">Performance</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/featureComparison.jsp">Features</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/specificationComparison.jsp">Specifications</a>
<a class="btn btn-secondary" href="<%=contextPath%>/CarSelectionServlet">Select More Cars</a>
<button type="button" class="btn btn-dark" onclick="window.print()">Print</button>
</div>

<%}%>
</main>
</body>
</html>
