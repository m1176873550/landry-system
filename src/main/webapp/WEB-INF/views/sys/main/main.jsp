<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>main-top</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/main/main.css?v=1.0">
</head>
<body style="overflow: hidden!important;width: 1338px">
<div id="main" v-cloak>
    <%--    导航--%>
    <%--    <div>--%>
    <%--        <el-breadcrumb separator-class="el-icon-arrow-right" v-cloak>--%>
    <%--            <el-breadcrumb-item><a href="/static/html/iframe-default.jsp">首页</a></el-breadcrumb-item>--%>
    <%--            <el-breadcrumb-item>{{permissionParentName}}</el-breadcrumb-item>--%>
    <%--            <el-breadcrumb-item>{{permissionName}}</el-breadcrumb-item>--%>
    <%--        </el-breadcrumb>--%>
    <%--    </div>--%>
    <%--    <hr class="myHr">--%>
    <div class="query">
        <el-menu :default-active="activeIndex"
                 class="el-menu-demo"
                 mode="horizontal"
                 @select="handleSelect"
                 v-cloak>
            <el-menu-item index="1" id="cloth-id">衣物类型</el-menu-item>
            <el-menu-item index="2" id="color-id">衣物颜色</el-menu-item>
            <el-menu-item index="3" id="service-id">服务类型</el-menu-item>
            <el-menu-item index="4" id="brand-id">衣物品牌</el-menu-item>
            <el-menu-item>
                <div class="input-button">
                    <el-input
                    <%--                            style="width: auto;font-size: 12px;"--%>
                            placeholder="衣物搜索"
                            v-model="query.key"
                            clearable
                            @focus="changeKey"
                            @change="queryKey"
                            @keyup.enter.native="queryKey">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="listColor">
                        </el-button>
                    </el-input>
                </div>
            </el-menu-item>
            <el-menu-item>
                <div class="right-button">
                    <el-button slot="append"
                               type="primary"
                               @click="resetAll">重置
                    </el-button>
                </div>
            </el-menu-item>
        </el-menu>
    </div>
    <div class="main-menu" id="main-menu">
        <div class="main-cascader" v-show="menuKey==='1'">
            <el-menu v-cloak
                     default-active="1-4-1"
                     class="el-menu-vertical-demo"
                     @select="handleSelectCloth">
                <div v-for="(cloth, index) in clothParentOptions" :key="index" class="text item">
                    <el-menu-item :index="cloth.id">
                        <span slot="">{{cloth.name}}</span>
                    </el-menu-item>
                </div>
            </el-menu>
        </div>
        <div class="main-button" v-show="menuKey==='1'" id="main-button1">
            <div class="text item">
                <div class="button-inline">
                    <el-button v-for="(cloth, index) in clothList"
                               :key="index"
                               @click="writeCloth(cloth)"
                               class="for-button" v-cloak>
                        {{cloth.name}}
                    </el-button>
                </div>
            </div>
        </div>
        <div class="main-button" v-show="menuKey==='2'" id="main-button2">
            <div class="text item">
                <div class="button-inline">
                    <el-button v-for="(color, index) in colorList"
                               :key="index"
                               @click="writeColor(color)"
                               class="for-button"
                               v-cloak>
                        {{color.name}}
                    </el-button>
                </div>
            </div>
        </div>
        <div class="main-button" v-show="menuKey==='3'" id="main-button3">
            <div class="text item">
                <div class="button-inline">
                    <el-button v-for="(service, index) in serviceList"
                               :key="index"
                               @click="writeService(service)"
                               class="for-button">
                        {{service.name}}
                    </el-button>
                </div>
            </div>
        </div>
        <div class="main-button" v-show="menuKey==='4'" id="main-button4">
            <div class="text item">
                <div class="button-inline">
                    <el-button v-for="(brand, index) in brandList"
                               :key="index"
                               @click="writeBrand(brand)"
                               class="for-button"
                               v-cloak>
                        {{brand.name}}
                    </el-button>
                </div>
            </div>
        </div>
    </div>
    <div class="main-order">
        <el-table
                :data="hangingPointList"
                border
                ref="hangingPointListRef"
                @selection-change="handleSelectionChange"
                style="width: 100%;border-top-width: 0;border-radius: 10px"
                height="250"
                v-cloak>
            <el-table-column
                    type="selection"
                    width="55"
                    fixed="left"
                    style="margin-left: 5px;">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="80"
                    fixed>
                <template slot-scope="scope">
                    <el-button
                            type="text"
                            @click="showDel(scope.row)"
                            class="el-icon-delete"
                            title="删除">
                    </el-button>
                </template>
            </el-table-column>
<%--            <el-table-column--%>
<%--                    prop="number"--%>
<%--                    sortable--%>
<%--                    fixed="left"--%>
<%--                    label="挂点号"--%>
<%--                    v-if="isQuery"--%>
<%--                    width="100">--%>
<%--            </el-table-column>--%>
            <el-table-column
                    prop="clothType"
                    label="衣物类型"
                    width=200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input @focus="handleInputFocus(scope.row,'cloth')"
                                  placeholder="衣物类型"
                                  disabled
                                  v-model="scope.row.clothType">
                        </el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="color"
                    label="颜色"
                    width=200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input @focus="handleInputFocus(scope.row,'color')"
                                  placeholder="颜色"
                                  v-model="scope.row.color">
                        </el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="service"
                    label="服务类型"
                    width="200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input @focus="handleInputFocus(scope.row,'service')"
                                  placeholder="服务类型"
                                  v-model="scope.row.service">
                        </el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="brand"
                    label="品牌"
                    width="200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input @focus="handleInputFocus(scope.row,'brand')"
                                  placeholder="品牌"
                                  v-model="scope.row.brand">
                        </el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="price"
                    label="价格"
                    sortable
                    width=200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input @focus="handleInputFocus(scope.row,'price')"
                                  @change="listPriceChange(scope.row,'price')"
                                  :disabled="!showBtn('laundry-edit-price')"
                                  placeholder="价格"
                                  v-model="scope.row.price">
                        </el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="des"
                    label="描述"
                    min-width=200">
                <template slot-scope="scope">
                    <div class="input-box">
                        <el-input placeholder="描述" v-model="scope.row.des"></el-input>
                    </div>
                </template>
            </el-table-column>
            <%--            <el-table-column--%>
            <%--                    prop="pickingTime"--%>
            <%--                    label="预计取衣时间"--%>
            <%--                    sortable--%>
            <%--                    width="220">--%>
            <%--            </el-table-column>--%>

        </el-table>
    </div>
    <div class="main-bottom" id="main-bottom">
        <el-form :inline="true"
                 :model="orderData"
                 ref="orderDataRef"
                 :rules="orderDataRules"
                 class="demo-form-inline"
                 v-cloak>
            <el-row :gutter="5" class="row-css">
                <span class="left-span">
                    <div class="operating-form-left">
                        <el-form-item class="price-label" label="应收" prop="receivePayment">
                            <el-input class="price-input"
                                      v-model="orderData.receivePayment"
                                      disabled
                                      @blur="priceFormat('receive')">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="元"></el-form-item>
                        <el-form-item label="实收" prop="actualPayment">
                            <el-input class="price-input"
                                      @focus="selectAll"
                                      :disabled="!showBtn('laundry-edit-price')"
                                      @change="priceChange"
                                      v-model="orderData.actualPayment"
                                      @blur="priceFormat('actual')">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="元"></el-form-item>
                        <el-form-item label="姓名" prop="name">
                            <el-autocomplete
                                    class="inline-input"
                                    v-model="orderData.name"
                                    :fetch-suggestions="queryNameSearch"
                                    @focus="selectAll"
                                    placeholder="姓名"
                                    clearable
                                    :highlight-first-item="true"
                                    select-when-unmatched
                                    placement="top"
                                    :trigger-on-focus="false"
                                    @select="querySearchSelect"
                            ></el-autocomplete>
                        </el-form-item>
                        <el-form-item label="电话" prop="phone">
                            <el-autocomplete
                                    class="inline-input"
                                    @focus="selectAll"
                                    v-model="orderData.phone"
                                    clearable
                                    :highlight-first-item="true"
                                    placement="top"
                                    select-when-unmatched
                                    :fetch-suggestions="queryPhoneSearch"
                                    :trigger-on-focus="false"
                                    placeholder="电话"
                                    @select="setOrderDataName"
                            ></el-autocomplete>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="checkVip">会员检索</el-button>
                        </el-form-item>
                        <el-form-item label="取衣时间" v-cloak>
                            <div class="time-label">
                                {{orderData.pickingTime}}
                            </div>
                            <el-button type="text" icon="el-icon-edit" @click="showEditDays"></el-button>
                        </el-form-item>
                        <el-form-item v-cloak prop="isPayed" class="surePay">
                            <template>
                                <el-checkbox v-model="orderData.isPayed">是否付款</el-checkbox>
                            </template>
                        </el-form-item>
                        <el-form-item label="打印" style="margin-left: 20px;" prop="printTimes">
                            <el-select class="choose-input"
                                       @change="changePrintTimes"
                                       v-model="orderData.printTimes"
                                       placeholder="打印方式">
                                <el-option label="打印一份" :value="1"></el-option>
                                <el-option label="打印两份" :value="2"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="支付" style="margin-left: 11px">
                            <el-select class="choose-input" v-model="orderData.payMethod" placeholder="支付方式">
                                <el-option
                                        v-for="payMethod in payMethods"
                                        :key="payMethod.id"
                                        :label="payMethod.name"
                                        :value="payMethod.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="showPayPicture">打码付款</el-button>
                        </el-form-item>
                    </div>
                </span>
                <span class="right-span">
                    <div class="operating-form-right">
                        <el-form-item>
                            <el-button type="primary" class="pay-button" @click="onSubmit">结账</el-button>
                        </el-form-item>
                    </div>
                </span>
            </el-row>
            <el-row :gutter="5" class="row-css" v-show="isCheckVip">
                <div class="main-bottom-info">
                    <div class="main-bottom-info-context">
                        <el-form-item label="姓名:">{{customerInfo.fullName}}，</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="会员编号:">{{customerInfo.nic}}，</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="余额:">{{customerInfo.balance}}元，</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="折扣:">{{customerInfo.discount}}</el-form-item>
                    </div>
                </div>
            </el-row>
        </el-form>
    </div>
    <%--        编辑取衣时间天数弹框--%>
    <div class="packing-time-dialog">
        <el-dialog title="取衣时间设置"
                   :visible.sync="setDaysDialogVisible"
                   :close-on-click-modal="false"
                   :before-close="closeSetDay"
                   :height="myTableHeight"
                   v-cloak>
            <el-form :model="dictionary"
                     :rules='dctRules'
                     label-width="100px"
                     ref="dictionaryRef"
                     class="demo-ruleForm">
                <el-form-item label="待取天数" prop="value">
                    <template>
                        <el-input-number v-model="dictionary.value"
                                         :min="1"
                                         :max="9999"
                                         label="待取天数">
                        </el-input-number>
                    </template>
                </el-form-item>
                <el-form-item label="描述" prop="des">
                    <el-input type="textarea"
                              :rows="2"
                              placeholder="请输入内容"
                              v-model="dictionary.des">
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeSetDay" v-cloak>取 消</el-button>
                <el-button type="primary" @click="editPackingTime" v-cloak>保 存</el-button>
            </div>
        </el-dialog>
    </div>
</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/main/main.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/static/js/common/CLodopFuncs.js"></script>
</html>
