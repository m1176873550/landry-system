<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>结账列表</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/orders/orders.css?v=1.0">
</head>
<body >

<div id="orders" style="height: 97%" v-cloak>

    <div class="query" style="margin-top:10px;padding-bottom: 10px;height: 30px">
        <el-row :gutter="5">
            <span style="position:absolute; left:34px;width: auto;height: 40px">
                <el-button type="primary" @click="handleSubmit">结账</el-button>
            </span>
        </el-row>
    </div>

    <%--    列表--%>
    <div id="myTableDiv" class="myTableDiv">
        <el-table
                :data="ordersList"
                border
                ref="ordersListRef"
                size="small"
                style="width: 100%;border-top-width: 0;border-radius: 10px">
            <el-table-column
                    prop="amount"
                    label="金额"
                    width="300">
            </el-table-column>
            <el-table-column
                    prop="des"
                    show-overflow-tooltip
                    label="备注"
                    min-width="130">
            </el-table-column>
            <el-table-column
                    prop="createdAt"
                    label="结账时间"
                    sortable="custom"
                    width="300">
            </el-table-column>
        </el-table>
        <div class="myPaginationDiv">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="query.currentPage"
                    :page-sizes="[10,20,30,40]"
                    :page-size="query.size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="query.total"
                    class="myPagination">
            </el-pagination>
        </div>
        <hr style="height: 50px;width: 100%;border: 0;margin: 0"/>
    </div>

    <el-dialog title="结账" :close-on-click-modal="false" :visible.sync="submitDialog" :before-close="handleClose"
               width="500px">
        <el-form class="demo-ruleForm" :model="ordersLog" :rules='rules' label-width="100px" ref="ordersLogRef">
            <el-alert
                    title="未计算会员充值金额"
                    type="info"
                    style="margin-bottom: 5px"
                    show-icon>
            </el-alert>
            <el-form-item label="金额" prop="amount">
                <el-input v-model="ordersLog.amount"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="des">
                <el-input v-model="ordersLog.des"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="handleChance">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </div>
    </el-dialog>
</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/orders/orders-log-list.js?v=1.0"></script>
</html>
