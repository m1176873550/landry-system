<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>数据分析</title>
    <%@include file="../../common/common.jsp" %>
    <!-- 引入 echarts.min.js -->
    <style>
        .orders-count,.income-summary,.bg-pic{
            margin: 20px;
        }
        [v-cloak] {
            display: none;
        }
        hr.style12{
            border: 0;
            height: 1px;
            background-image: -webkit-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0);
            background-image: -moz-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0);
            background-image: -ms-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0);
            background-image: -o-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0);
        }
        hr.style13 {
            height: 10px;
            border: 0;
            box-shadow: 0 10px 10px -10px #8c8b8b inset;
        }
        .bg-pic {
            background:url(/static/images/sys/analysis-bg.png) no-repeat center;
            background-size:100% 100%;
        }
    </style>
</head>
<body>
<div id="summaryGraph" v-cloak>
    <div class="orders-count">
        <div>【订单统计】</div>
        <hr style="height:10px;border:none;border-top:10px groove skyblue;" />
        <el-radio-group v-model="radioOrders" @change="handleDropDown">
            <el-radio label="0">昨日</el-radio>
            <el-radio label="1">最近一周</el-radio>
            <el-radio label="2">最近一月</el-radio>
            <el-radio label="3">最近一年</el-radio>
            <el-radio label="4">全部统计</el-radio>
        </el-radio-group>
        <hr style="height:10px;border:none;border-top:10px groove skyblue;" />
    </div>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div class="bg-pic">
        <div id="dataSummary" style="width: auto;height:400px;"></div>
    </div>
    <hr class="style12">

    <div class="income-summary">
        <div>【月收入统计】</div>
        <hr style="height:10px;border:none;border-top:10px groove skyblue;" />
        <el-radio-group v-model="radioIncome" @change="handleChangeIncome">
            <el-radio label="0">最近一年</el-radio>
            <el-radio label="1">最近两年</el-radio>
            <el-radio label="2">最近三年</el-radio>
            <el-radio label="3">全部统计</el-radio>
        </el-radio-group>
        <hr style="height:10px;border:none;border-top:10px groove skyblue;" />
    </div>
    <div class="bg-pic">
        <div id="incomeSummary" style="width: auto;height:400px;" ></div>
    </div>
</div>

</body>
<script src="${pageContext.request.contextPath}/static/js/common/echarts.min.js"></script>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/data-analysis/summary-graph.js?v=1.0"></script>
</html>