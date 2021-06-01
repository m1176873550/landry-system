<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>信息提醒</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/remind/remind.css?v=1.0">
</head>
<body style="overflow: auto">

<div id="brandList">
    <div class="query" style="margin-top:10px;padding-bottom: 10px;height: 30px;text-align: center;">
        <div class="top-div">
            <el-switch
                    style="display: block"
                    @change="handleChangeOnMsg"
                    v-model="onMsg"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-text="开启短信"
                    inactive-text="关闭短信">
            </el-switch>
        </div>
        <div class="left-div" v-cloak>
            <div class="cornsilk-bg">
                <el-button type="success" style="margin-left:5px;float: left" @click.native="sendVips">群发短信</el-button>
            </div>
            <el-table
                    :data="vipList"
                    border
                    ref="vipListRef"
                    :row-class-name="vipTableRowClassName"
                    height="360"
                    @selection-change="handleSelectionVipChange"
                    v-cloak>
                <el-table-column
                        type="selection"
                        width="55"
                        fixed="left"
                        style="margin-left: 5px;">
                </el-table-column>
                <el-table-column
                        prop="fullName"
                        label="用户名"
                        min-width="100">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="手机号"
                        min-width="100">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        label="生日"
                        min-width="100">
                </el-table-column>
            </el-table>
            <div class="myPaginationDiv">
                <el-pagination
                        @size-change="handleVipSizeChange"
                        @current-change="handleVipCurrentChange"
                        :current-page="queryVip.pageNum"
                        :page-sizes="[5,10,15,20]"
                        :page-size="queryVip.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="queryVip.total"
                        class="myPagination">
                </el-pagination>
            </div>
        </div>

        <div class="right-div " v-cloak>
            <div class="cornsilk-bg">
                <el-button type="success" style="margin-left:5px;float: left" @click.native="sendPicks">群发短信</el-button>
            </div>
            <el-table
                    :data="pickList"
                    :row-class-name="pickTableRowClassName"
                    border
                    height="360"
                    ref="pickListRef"
                    @selection-change="handleSelectionPickChange"
                    v-cloak>
                <el-table-column
                        type="selection"
                        width="55"
                        fixed="left"
                        style="margin-left: 5px;">
                </el-table-column>
                <el-table-column
                        prop="payer"
                        label="付款人"
                        min-width="100">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="手机号"
                        min-width="100">
                </el-table-column>
                <el-table-column
                        prop="packingTime"
                        label="取衣时间"
                        min-width="100">
                </el-table-column>
            </el-table>
            <div class="myPaginationDiv">
                <el-pagination
                        @size-change="handlePickSizeChange"
                        @current-change="handlePickCurrentChange"
                        :current-page="queryPick.pageNum"
                        :page-sizes="[5,10,15,20]"
                        :page-size="queryPick.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="queryPick.total"
                        class="myPagination">
                </el-pagination>
            </div>
        </div>
    </div>

</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/remind/model.js?v=1.0"></script>
</html>
