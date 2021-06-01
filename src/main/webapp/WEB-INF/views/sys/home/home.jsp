<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 2020/2/11
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的干洗店</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/home/home.css?v=1.0">
</head>
<body style="overflow-x: hidden!important">
<div class="screen" id="screen">
    <div id="header" style="margin-bottom: 0px">
        <el-menu class="el-menu-demo"
        <%--                 :default-active="activeIndex"--%>
                 mode="horizontal"
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b"
                 @select="handleTopSelect"
                 v-cloak>
            <el-submenu index="1">
                <template slot="title">基础设置</template>
                <el-menu-item index="1-1">衣物挂点</el-menu-item>
                <el-menu-item index="1-2">店员信息</el-menu-item>
                <el-submenu index="1-3">
                    <template slot="title">衣物信息</template>
                    <el-menu-item index="1-3-1">衣物类型</el-menu-item>
                    <el-menu-item index="1-3-2">服务类型</el-menu-item>
                    <el-menu-item index="1-3-3">衣物颜色</el-menu-item>
                    <el-menu-item index="1-3-4">衣物品牌</el-menu-item>
                </el-submenu>
            </el-submenu>
            <el-submenu index="2">
                <template slot="title">会员管理</template>
                <el-menu-item index="2-1">顾客信息</el-menu-item>
                <el-menu-item index="2-2">会员充值</el-menu-item>
                <el-menu-item index="2-3">会员发行</el-menu-item>
            </el-submenu>
            <el-menu-item index="5"
                          id="query-change"
            <%--                          v-show="pageContent !=='/view/hanging-point/hanging-point-list.htm'"--%>
                          style="width: auto">
                <el-input
                        placeholder="票单号"
                        v-model="query.orderId"
                        clearable
                        @clear="clearQuery"
                        @keyup.enter.native="queryList">
                </el-input>
                <el-input
                        placeholder="手机号"
                        v-model="query.phone"
                        clearable
                        @clear="clearQuery"
                        @keyup.enter.native="queryList">
                </el-input>
                <el-input
                        placeholder="挂点号"
                        v-model="query.number"
                        @clear="clearQuery"
                        clearable
                        @keyup.enter.native="queryList">
                </el-input>
                <el-button
                        class="normal-button"
                        type="primary"
                        icon="el-icon-search"
                        @click="queryList">
                </el-button>
            </el-menu-item>
                <el-dropdown style="margin-top: 3px;float: right;right: 32px;" @command="handleUsernameDown">
                    <el-button class="right-mid-button">
                        {{username}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown" style="transform-origin: center top;z-index: 2005;height: 75px;display: none;">
                        <el-dropdown-item command="1">修改密码</el-dropdown-item>
                        <el-dropdown-item command="2">注销</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
        </el-menu>
        <hr class="top-line">
    </div>
    <div id="left">
        <div class="main-cascader">
            <el-menu v-cloak
                     default-active="2"
                     class="el-menu-vertical-demo"
                     background-color="#545c64"
                     text-color="#fff"
                     active-text-color="#ffd04b"
                     @select="handleUrlOpen">
                <div v-for="(urlOption, index) in urlOptions" :key="index" class="text item">
                    <el-menu-item :index="urlOption.url">
                        <span slot="title" >{{urlOption.name}}</span>
                    </el-menu-item>
                </div>
            </el-menu>
        </div>
    </div>
    <div id="right">
        <iframe :src="pageContent"
                id="iframe"
                class="no-border-iframe-class"
                width="100%"
                height="100%"></iframe>
    </div>
    <%--    1-3--%>
    <%--品牌--%>
    <el-dialog title="衣物品牌管理"
               :visible.sync="brandDialogVisible"
    <%--               class="addAndEdit"--%>
               :close-on-click-modal="false"
               :width="dialogWidth"
               :before-close="brandClose"
               :height="myTableHeight"
               v-cloak>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <%--                <span>衣物品牌</span>--%>

                <el-button style="float: left;"
                           type="primary"
                           v-show="toBAdd"
                           @click="toAddBrand">
                    新增
                </el-button>
                <div style="float:right;" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="衣物品牌"
                              v-model="query13.key"
                              clearable
                              @change="listBrand"
                              @keyup.enter.native="listBrand">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="listBrand">
                        </el-button>
                    </el-input>
                </div>
                <div style="margin-top: 0;float: left" class="input-button" v-show="openingBAdd">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="添加衣物品牌"
                              v-model="addBrandName"
                              clearable
                              @keyup.enter.native="addBrand">
                        <el-button slot="append"
                                   icon="el-icon-check"
                                   @click="addBrand">
                        </el-button>
                    </el-input>
                </div>
            </div>
            <div v-for="(brand, index) in brandList" :key="index" class="text item">
                <el-input style="width: auto;font-size: 12px;"
                          placeholder="修改衣物品牌"
                          v-model="brand.name"
                          clearable
                          @change="editBrand(brand)">
                </el-input>
            </div>
        </el-card>
    </el-dialog>
    <%--颜色--%>
    <el-dialog title="衣物颜色管理"
               :visible.sync="colorDialogVisible"
    <%--               class="addAndEdit"--%>
               :close-on-click-modal="false"
               :width="dialogWidth"
               :before-close="colorClose"
               :height="myTableHeight"
               v-cloak>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button style="float: left;"
                           type="primary"
                           v-show="toCAdd"
                           @click="toAddColor"
                >新增
                </el-button>
                <%--                <span>衣物颜色</span>--%>
                <div style="float: right;" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="衣物颜色"
                              v-model="query13.key"
                              clearable
                              @change="listColor"
                              @keyup.enter.native="listColor">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="listColor">
                        </el-button>
                    </el-input>
                </div>
                <div style="margin-top: 0;float: left" class="input-button" v-show="openingCAdd">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="添加衣物颜色"
                              v-model="addColorName"
                              clearable
                              @keyup.enter.native="addColor">
                        <el-button slot="append"
                                   icon="el-icon-check"
                                   @click="addColor">
                        </el-button>
                    </el-input>
                </div>
            </div>
            <div v-for="(color, index) in colorList" :key="index" class="text item">
                <el-input style="width: auto;font-size: 12px;"
                          placeholder="修改衣物颜色"
                          v-model="color.name"
                          clearable
                          @change="saveColor(color)"
                >
                </el-input>
            </div>
        </el-card>
    </el-dialog>
    <%--        服务类型窗口--%>
    <el-dialog title="衣物服务类型管理"
               :visible.sync="serviceDialogVisible"
    <%--               class="addAndEdit"--%>
               :width="dialogWidth"
               :close-on-click-modal="false"
               :before-close="serviceClose"
               :height="myTableHeight"
               v-cloak>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button style="float: left;"
                           type="primary"
                           v-show="toSAdd"
                           @click="toAddService"
                >新增
                </el-button>
                <div style="float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="服务类型"
                              v-model="query13.key"
                              clearable
                              @change="listService"
                              @keyup.enter.native="listService">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="listService">
                        </el-button>
                    </el-input>
                </div>
                <div style="margin-top: 0;float: left" class="input-button" v-show="openingSAdd">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="添加服务类型"
                              v-model="addServiceName"
                              clearable
                              @keyup.enter.native="addService">
                        <el-button slot="append"
                                   icon="el-icon-check"
                                   @click="addService">
                        </el-button>
                    </el-input>
                </div>
            </div>
            <div v-for="(service, index) in serviceList" :key="index" class="text item">
                <el-input style="width: auto;font-size: 12px;"
                          placeholder="修改服务类型"
                          v-model="service.name"
                          clearable
                          @change="saveService(service)">
                </el-input>
            </div>
        </el-card>
    </el-dialog>
    <%--       衣物类型窗口--%>
    <el-dialog title="衣物类型管理"
               :visible.sync="clothDialogVisible"
               :close-on-click-modal="false"
               :before-close="clothClose"
               :width="dialogWidth"
               :height="myTableHeight"
               v-cloak>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button style="float: left;"
                           type="primary"
                           v-show="toClothAdd"
                           @click="toAddCloth"
                <%--                           icon="el-icon-plus"--%>
                >新增
                </el-button>
                <div class="input-button" style="float: right;">
                    <el-input style="width: auto;font-size: 14px;"
                              placeholder="衣物类型"
                              v-model="query13.key"
                              clearable
                              @change="listCloth"
                              @keyup.enter.native="listCloth">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="listCloth">
                        </el-button>
                    </el-input>
                </div>
                <div class="input-button" style="float: left" v-show="openingClothAdd">
                    <el-select
                            v-model="addClothParent"
                            filterable
                            clearable
                            allow-create
                            @change="checkClothParent"
                            default-first-option
                            placeholder="衣物分类">
                        <el-option
                                v-for="clothParentOption in clothParentOptions"
                                :key="clothParentOption.id"
                                :label="clothParentOption.name"
                                :value="clothParentOption.name">
                        </el-option>
                    </el-select>
                    <span>-</span>
                    <el-input placeholder="衣物类型"
                              id="rectangle-input-long"
                    <%--                              style="width: 126px;margin-top: 1px;"--%>

                              v-model="addClothName"
                              clearable
                              @keyup.enter.native="addCloth">
                    </el-input>
                    <span>-</span>
                    <el-input
                    <%--                            style="width: auto;font-size: 12px;"--%>
                            id="rectangle-input"
                            placeholder="价格"
                            v-model="addClothPrice"
                            @keyup.enter.native="addCloth">
                        <el-button slot="append"
                                   icon="el-icon-check"
                                   @click="addCloth">
                        </el-button>
                    </el-input>

                </div>
            </div>
            <div v-for="(cloth, index) in clothList" :key="index" class="text item">
                <div class="second-input">
                    <el-select
                            filterable
                            clearable
                            default-first-option
                            @change="saveCloth(cloth)"
                            v-model="cloth.parentId"
                            placeholder="修改衣物分类">
                        <el-option
                                v-for="clothParentOption in clothParentOptions"
                                :key="clothParentOption.id"
                                :label="clothParentOption.name"
                                :value="clothParentOption.id">
                        </el-option>
                    </el-select>
                </div>
                <span>——</span>
                <div class="second-input">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="修改衣物类型"
                              v-model="cloth.name"
                              clearable
                              @change="saveCloth(cloth)">
                    </el-input>
                </div>
                <span>——</span>
                <div class="third-input">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="修改衣物价格"
                              v-model="cloth.price"
                              clearable
                              @change="saveCloth(cloth)">
                    </el-input>
                </div>
            </div>
        </el-card>
    </el-dialog>
    <%--    会员充值窗口--%>
    <el-dialog :title="title"
               :visible.sync="vipQueryDialogVisible"
               :close-on-click-modal="false"
               :before-close="handleClose"
               :width="dialogWidth"
               :height="myTableHeight"
               v-cloak>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="primary" @click="addVipToTrue">新增会员</el-button>
                <div class="input-button dialog-query-button">
                    <el-autocomplete
                            class="dialog-input-button"
                            @focus="listVipPhones"
                            v-model="queryVip.phone"
                            clearable
                            :highlight-first-item="true"
                            placement="top"
                            select-when-unmatched
                            :fetch-suggestions="queryPhoneSearch"
                            :trigger-on-focus="false"
                            placeholder="查询会员手机号"
                            @select="setCustomerValue">
                        <el-button slot="append"
                                   icon="el-icon-search"
                                   @click="queryCustomer">
                        </el-button>
                    </el-autocomplete>
                    </el-input>
                </div>
            </div>
            <el-form :model="customer" :rules='customerRules' label-width="100px" ref="customerRef"
                     class="demo-ruleForm">
                <el-form-item label="姓名" prop="fullName">
                    <el-autocomplete
                            class="dialog-input-button"
                            v-model="customer.fullName"
                            :fetch-suggestions="queryNameSearch"
                            @focus="listCustomer"
                            placeholder="顾客姓名"
                            clearable
                            :highlight-first-item="true"
                            select-when-unmatched
                            placement="top"
                            :trigger-on-focus="false"
                            @select="setCustomerPhone"
                    ></el-autocomplete>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-autocomplete
                            class="dialog-input-button"
                            @focus="listPhones"
                            v-model="customer.phone"
                            clearable
                            :highlight-first-item="true"
                            placement="top"
                            select-when-unmatched
                            :fetch-suggestions="queryPhoneSearch"
                            :trigger-on-focus="false"
                            placeholder="顾客电话"
                            @select="setCustomerName"
                    ></el-autocomplete>
                    <el-button type="primary" @click="checkVip">会员检索</el-button>
                </el-form-item>
                <div class="input-and-button">
                    <el-form-item label="会员号" prop="nic">
                        <el-input class="dialog-input-button"
                                  v-model="customer.nic"
                                  disabled
                                  clearable></el-input>
                        <el-button type="primary" v-show="addVip" @click="generateNic"
                                   icon="el-icon-refresh-left"></el-button>
                    </el-form-item>
                </div>
                <el-form-item label="折扣" prop="discount" v-show="customer.nic!==''">
                    <el-input v-model="customer.discount" clearable></el-input>
                </el-form-item>
                <el-form-item label="余额" prop="balance">
                    <el-input v-model="customer.balance" disabled></el-input>
                </el-form-item>
                <el-form-item label="充值" prop="rechargeAmount" v-if="customer.nic!==''">
                    <el-input v-model="customer.rechargeAmount"></el-input>
                </el-form-item>
                <el-form-item label="生日" prop="birthday" :label-width="'100px'" v-if="title ==='会员发行'">
                    <el-date-picker
                            v-model="customer.birthday"
                            align="left"
                            type="date"
                            clearable
                            placeholder="选择日期"
                            :picker-options="pickerOptions"
                            value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="地址" prop="address" v-if="title ==='会员发行'">
                    <el-input style="width:300px"
                              type="textarea"
                              :rows="2"
                              placeholder="地址"
                              v-model="customer.address"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="des" >
                    <el-input style="width:300px"
                              type="textarea"
                              :rows="2"
                              placeholder="描述"
                              v-model="customer.des"
                              clearable>
                    </el-input>
                </el-form-item>
            </el-form>
        </el-card>
        <div slot="footer" class="dialog-footer">
            <el-button @click="chanceClose">取 消</el-button>
            <el-button type="primary"  @click="customerRecharge">保 存</el-button>
        </div>
    </el-dialog>

    <div class="changePwd">
        <el-dialog title="修改密码" :close-on-click-modal="false" :center="true" :before-close="handleUpdatePwdClose"
                   :visible.sync="dialogFormVisible" width="415px" class="modifyClass" v-cloak>
            <div class="update-pwd">
                <el-form class="demo-ruleForm" :model="updatePassword" :rules="rules" label-width="100px"
                         ref="updatePassword"
                         style="padding-left: 0;">
                    <div class="pwd">
                        <el-form-item label="新密码" prop="password">
                            <el-input class="pwd-input"
                                      type="password"
                                      v-model="updatePassword.password">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="确认密码" prop="newPassword">
                            <el-input class="pwd-input"
                                      type="password"
                                      v-model="updatePassword.newPassword">
                            </el-input>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer" style="width: 550px;padding-top: 0;margin-left: -28px;">
                <el-button @click="resetForm">重 置</el-button>
                <el-button type="primary" @click="editPassword">确 定</el-button>
            </div>
        </el-dialog>
    </div>

</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/home/home.js?v=1.0"></script>
</html>
