<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.LinkedHashSet,java.util.Set,com.carzo.model.Car,com.carzo.model.ComparisonResult"%>

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

    List<Car> cars=result!=null?result.getCars():null;
    Set<String> allFeatures=new LinkedHashSet<>();

    if(cars!=null){
        for(Car car:cars){
            if(car!=null&&car.getFeatures()!=null){
                for(String feature:car.getFeatures()){
                    if(feature!=null&&!feature.trim().isEmpty()){
                        allFeatures.add(feature.trim());
                    }
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
<title>Compare Cars | Carzo</title>
<link rel="stylesheet" href="<%=contextPath%>/comparison/comparison.css">
<style>
*{box-sizing:border-box}
body{margin:0;background:#f4f6fb;font-family:Arial,sans-serif;color:#202a3b}
.carzo-header{background:linear-gradient(110deg,#101b35,#263c63);color:white;padding:18px 7%;display:flex;justify-content:space-between;align-items:center}
.carzo-logo{font-size:30px;font-weight:800;color:#ff424f}
.carzo-nav{display:flex;gap:28px}
.carzo-nav a{color:white;text-decoration:none;font-weight:600}
.carzo-nav a:hover{color:#ff5964}
.comparison-container{width:90%;max-width:1350px;margin:35px auto}
.page-title{text-align:center;color:#172440;font-size:34px}
.page-subtitle{text-align:center;color:#69758a;margin-bottom:35px}
.section-title{color:#172440;font-size:23px;margin:35px 0 20px;border-left:5px solid #ed3545;padding-left:12px}
.car-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(230px,1fr));gap:22px}
.car-card{background:white;border-radius:14px;overflow:hidden;box-shadow:0 5px 20px #14234117}
.car-image{height:180px;background:#e9edf4;display:flex;align-items:center;justify-content:center}
.car-image img{width:100%;height:100%;object-fit:contain}
.car-content{padding:20px}
.brand-name{color:#ed3545;font-weight:700;text-transform:uppercase;font-size:13px}
.car-name{color:#172440;font-size:21px}
.car-price{color:#ed3545;font-weight:700;font-size:17px}
.comparison-table{width:100%;overflow-x:auto;border:1px solid #e5eaf1;border-radius:10px;margin-bottom:25px}
table{width:100%;min-width:550px;border-collapse:collapse;background:white}
th,td{padding:14px 16px;text-align:center;border-bottom:1px solid #e8edf3}
th{background:#101c35;color:white}
td:first-child{background:#f4f6fb;font-weight:700;text-align:left}
.feature-available{color:#16834a;font-weight:700}
.feature-unavailable{color:#d83b45;font-weight:700}
.recommendation-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(210px,1fr));gap:18px}
.recommendation-card{background:#f8faff;border:1px solid #e3e9f2;border-top:4px solid #e63946;padding:20px;border-radius:10px}
.recommendation-card h3{color:#e63946}
.button-container{display:flex;justify-content:center;flex-wrap:wrap;gap:13px;margin:35px 0}
.btn{display:inline-block;padding:12px 21px;border:0;border-radius:7px;text-decoration:none;font-weight:700;cursor:pointer}
.btn-primary{background:#e63946;color:white}
.btn-secondary{background:#101c35;color:white}
.error-message{background:#fff0f0;color:#c62828;padding:18px;text-align:center;border-radius:8px}
@media(max-width:700px){.carzo-header{flex-direction:column;gap:15px}.carzo-nav{gap:15px}.comparison-container{width:95%}.button-container{flex-direction:column}}
@media print{.carzo-header,.button-container{display:none}.comparison-container{width:100%;margin:0}}
</style>
</head>

<body>
<header class="carzo-header">
<div class="carzo-logo">Carzo</div>
<nav class="carzo-nav">
<a href="<%=contextPath%>/CarSelectionServlet">Home</a>
<a href="<%=contextPath%>/CarSelectionServlet">Cars</a>
<a href="<%=contextPath%>/CarRecommendationServlet">Recommendations</a>
</nav>
</header>

<main class="comparison-container">
<h1 class="page-title">Car Comparison</h1>
<p class="page-subtitle">Compare prices, performance, features and specifications.</p>

<%if(cars==null||cars.size()<2){%>
<div class="error-message">No comparison data available. Please select at least two cars.</div>
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
<p class="brand-name"><%=car.getBrandName()!=null?car.getBrandName():""%></p>
<h3 class="car-name"><%=car.getCarName()%></h3>
<p class="car-price">₹<%=String.format("%.2f",car.getMinPrice())%> - ₹<%=String.format("%.2f",car.getMaxPrice())%> Lakh</p>
</div>
</div>

<%}%>
</div>

<h2 class="section-title">Comparison Summary</h2>
<div class="comparison-table">
<table>
<tr><th>Specification</th><%for(Car car:cars){%><th><%=car.getCarName()%></th><%}%></tr>
<%
String[][] labels={
    {"Brand","brand"},
    {"Category","category"},
    {"Condition","condition"},
    {"Minimum Price","min"},
    {"Maximum Price","max"},
    {"Mileage","mileage"},
    {"Engine Capacity","engine"},
    {"Transmission","transmission"},
    {"Seating Capacity","seats"},
    {"Airbags","airbags"},
    {"Launch Status","launch"},
    {"Maximum Power","power"},
    {"Maximum Torque","torque"},
    {"Length","length"},
    {"Width","width"},
    {"Height","height"},
    {"Wheelbase","wheelbase"},
    {"Ground Clearance","ground"},
    {"Boot Space","boot"}
};
for(String[] item:labels){
%>
<tr><td><%=item[0]%></td>
<%for(Car car:cars){String value="Not Available";
switch(item[1]){
case "brand":value=car.getBrandName();break;
case "category":value=car.getCategoryName();break;
case "condition":value=car.getCarCondition();break;
case "min":value="₹"+String.format("%.2f",car.getMinPrice())+" Lakh";break;
case "max":value="₹"+String.format("%.2f",car.getMaxPrice())+" Lakh";break;
case "mileage":value=car.getMileage()+" km/l";break;
case "engine":value=car.getEngineCapacity()+" cc";break;
case "transmission":value=car.getTransmission();break;
case "seats":value=String.valueOf(car.getSeatingCapacity());break;
case "airbags":value=String.valueOf(car.getAirbags());break;
case "launch":value=car.getLaunchStatus();break;
case "power":value=car.getMaxPower();break;
case "torque":value=car.getMaxTorque();break;
case "length":value=car.getLengthMm()+" mm";break;
case "width":value=car.getWidthMm()+" mm";break;
case "height":value=car.getHeightMm()+" mm";break;
case "wheelbase":value=car.getWheelbaseMm()+" mm";break;
case "ground":value=car.getGroundClearanceMm()+" mm";break;
case "boot":value=car.getBootSpaceL()+" L";break;
}
if(value==null||value.trim().isEmpty())value="Not Available";
%><td><%=value%></td><%}%>
</tr>
<%}%>
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
<div class="recommendation-card"><h3>Best Mileage</h3><p><strong><%=result.getBestMileageCar().getCarName()%></strong></p><p><%=result.getBestMileageCar().getMileage()%> km/l</p></div>
<%}if(result.getBestEngineCar()!=null){%>
<div class="recommendation-card"><h3>Best Engine</h3><p><strong><%=result.getBestEngineCar().getCarName()%></strong></p><p><%=result.getBestEngineCar().getEngineCapacity()%> cc</p></div>
<%}if(result.getBestSafetyCar()!=null){%>
<div class="recommendation-card"><h3>Best Safety</h3><p><strong><%=result.getBestSafetyCar().getCarName()%></strong></p><p><%=result.getBestSafetyCar().getAirbags()%> Airbags</p></div>
<%}if(result.getBestFeatureCar()!=null){%>
<div class="recommendation-card"><h3>Best Features</h3><p><strong><%=result.getBestFeatureCar().getCarName()%></strong></p><p><%=result.getBestFeatureCar().getFeatures()!=null?result.getBestFeatureCar().getFeatures().size():0%> Features</p></div>
<%}%>
</div>

<div class="button-container">
<a class="btn btn-primary" href="<%=contextPath%>/comparison/priceComparison.jsp">Price Comparison</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/performanceComparison.jsp">Performance</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/featureComparison.jsp">Features</a>
<a class="btn btn-primary" href="<%=contextPath%>/comparison/specificationComparison.jsp">Specifications</a>
<a class="btn btn-secondary" href="<%=contextPath%>/CarSelectionServlet">Compare Other Cars</a>
<button class="btn btn-secondary" type="button" onclick="window.print()">Print</button>
</div>

<%}%>
</main>
</body>
</html>
