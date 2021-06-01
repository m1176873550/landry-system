var vm = new Vue({
    el: '#main',
    created() {
        var _this = this;
        //字典表查询打印次数
        _this.getPrintTimes();
    },
    mounted() {
        var _this = this;
        _this.queryKey();
        //顾客手机号名字
        _this.listCustomer();
        _this.listPhones();
        //获取字典设置的待取衣天数
        _this.getDays();

        //设置右菜单宽度
        var mainMenu = document.getElementById("main-menu");
        if (mainMenu !== undefined && mainMenu !== null) {
            _this.mainMenuWidth = mainMenu.offsetWidth * 0.8;
            var mainButton1 = document.getElementById("main-button1");
            var mainButton2 = document.getElementById("main-button2");
            var mainButton3 = document.getElementById("main-button3");
            var mainButton4 = document.getElementById("main-button4");
            mainButton1.style.width = _this.mainMenuWidth;
            mainButton2.style.width = _this.mainMenuWidth;
            mainButton3.style.width = _this.mainMenuWidth;
            mainButton4.style.width = _this.mainMenuWidth;
        }
        var mainBottom = document.getElementById("main-bottom");
        if (mainBottom !== undefined && mainBottom !== null) {
            _this.mainBottomWidth = mainBottom.offsetWidth * 0.8;
            mainBottom.style.width = _this.mainBottomWidth;
        }

    },
    updated: function () {
        var _this = this;
        if (_this.$refs["hangingPointListRef"] !== null) {
            _this.$nextTick(function () {
                this.$refs.hangingPointListRef.doLayout();
            })
        }
    },
    data: function () {
        var checkPhone = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            let pattern = new RegExp(/^[1]([3-9])[0-9]{9}$/);  // 9位手机号
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('手机号格式错误'));
            }
        };
        var checkName = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            var pattern = new RegExp(/^[A-Za-z0-9\u4e00-\u9fa5]+$/);  // 英文/数字/汉字
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('顾客姓名格式错误'));
            }
        };
        var checkPrice = (rule, value, callback) => {    // 验证预算
            if (value) {
                if (/^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/.test(value)) {
                    callback();
                } else {
                    callback(new Error('金额格式错误'));
                }
            } else {
                callback(new Error('金额不能为空'));
            }
        };
        return {
            query: {
                key: '',
                id: '',
            },
            brandList: [],       //列表
            brand: {
                id: '',
                name: '',
            },                   //新增衣物的对象
            addBrandName: '',
            //color
            colorList: [],           //颜色列表
            color: {
                id: '',
                name: '',
            },               //单个颜色对象
            addColorName: '',
            //service
            serviceList: [],           //服务列表
            service: {
                id: '',
                name: '',
            },               //单个服务类型对象
            //cloth
            clothList: [],           //服务列表
            cloth: {
                id: '',
                name: '',
                price: '',
                parent: '',//添加衣物父类名
            },               //单个服务类型对象
            clothParentOptions: [],
            clothParentOption: {},
            //cloth
            myTableHeight: '70%',
            options: [],
            //导航
            isCollapse: true,
            //菜单右
            mainMenuWidth: 0,
            activeIndex: '1',
            menuKey: '1',
            hangingPoint: {
                number: '',
                clothTypeId: '',
                clothType: '',
                price: '',
                payer: '',
                phone: '',
                des: '',
                pickingTime: this.getLocalDateTime("yyyy-MM-dd HH:mm:ss"),  //当前时间+N天
            },
            hangingPointList: [],           // 订单列表,
            customerList: [],                //顾客名称电话对象列表
            phoneList: [],                  //号码列表
            multipleSelection: [],          //接受多选对象
            isQuery: false,                 //是否搜索，展示挂点
            orderVo: {
                ordersId: '',
                actualPayment: '',
            },
            orderData: {
                ordersId: '',
                receivePayment: '',
                actualPayment: '',
                name: '',
                phone: '',
                printTimes: 2,
                isPayed: false,
                nic: '',
                pickingTime: this.getLocalDateTime("yyyy-MM-dd HH:mm:ss"),  //当前时间+N天
                payMethod: 'aliPay',
                hangingPoints: [],
            },
            payMethods: [{
                id: 'aliPay',
                name: '支付宝'
            }, {
                id: 'others',
                name: '其它',
            }],
            setDaysDialogVisible: false,
            dictionary: {
                name: 'packingTimeDays',
                value: 3,
                des: '',
            },
            dctPrintTimes: {
                name: PRINTTIMES,
                value: 2,
            },
            customerInfo: {
                fullName: '',
                balance: '',
                nic: '',
                discount: '1.0',
            },
            isCheckVip: false,           //是否VIP查询，展示VIP信息
            reacquireTimes: 0,
            orderDataRules: {              //订单验证规则
                // payMethod: [
                //     {required: true, message: '付款方式不能为空', trigger: 'blur'},
                // ],
                // printTimes: [
                //     {required: true, message: '打印次数不能为空', trigger: 'blur'},
                // ],
                name: [
                    {required: true, message: '付款人不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '付款人格式错误', trigger: 'change'},
                    {validator: checkName, trigger: 'change'}
                ],
                actualPayment: [
                    {required: true, message: '实收金额不能为空', trigger: 'change'},
                    {validator: checkPrice, message: '金额格式错误', trigger: 'blur'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'change'}
                ],
            },
            dctRules: {              //字典天数校验规则
                value: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                ]
            },
        }
    },
    methods: {
        printTicket: function () {
            var _this = this;
            var nowDate = new Date();
            let pointLength = _this.orderData.hangingPoints.length;

            //初始化打印机
            var LODOP = getCLodop();
            LODOP.PRINT_INIT("打印");               //首先一个初始化语句
            LODOP.SET_PREVIEW_WINDOW(1, 3, 0, 1000, 768);
            LODOP.ADD_PRINT_RECT("0%","0%",270,170 + 25 * (pointLength + 8),0,1);
            // blDirectPrint： 打印按钮是否“直接打印” 1-是  0-否（弹出界面“选机打印”）
            LODOP.SET_PRINT_STYLE("FontSize", 25); //字体大小
            // LODOP.ADD_PRINT_IMAGE(220,50,150,150,"<img border='0' src='../static/images/login/logo.png' width='150' height='150'/>"); //打印二维码
            LODOP.ADD_PRINT_TEXT(20, 75, 150, 20, "干洗店");
            LODOP.SET_PRINT_STYLE("FontSize", 15); //字体大小
            LODOP.ADD_PRINT_TEXT(50, 15, 250, 20, "----------------------------------");
            LODOP.ADD_PRINT_TEXT(70, 65, 230, 20, "票单号：" + _this.orderData.ordersId); //然后多个ADD语句及SET语句
            LODOP.SET_PRINT_STYLE("FontSize", 10); //字体大小
            LODOP.ADD_PRINT_TEXT(95, 15, 230, 20, "打印时间：" + nowDate.getFullYear() + "-"
                + (nowDate.getMonth() + 1) + "-" + nowDate.getDate() + " " + nowDate.getHours() + ":" + nowDate.getMinutes()
                + ":" + nowDate.getSeconds());
            LODOP.ADD_PRINT_TEXT(120, 15, 260, 20, "----------------------------------");
            LODOP.ADD_PRINT_TEXT(145, 15, 230, 20, "挂点 金额 支付 衣物类型 颜色 ");
            for (var i = 0; i < pointLength; i++) {
                LODOP.ADD_PRINT_TEXT(170 + 25 * i, 15, 230, 20, _this.orderData.hangingPoints[i].number + '   ' +
                    _this.orderData.hangingPoints[i].price + '  ' + (this.orderData.hangingPoints[i].isPayed === 1 ? '已付款' : '未付') + ' ' +
                    _this.orderData.hangingPoints[i].clothType + ' ' + (_this.orderData.hangingPoints[i].color === null ? ' ' : _this.orderData.hangingPoints[i].color));
            }
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength), 15, 260, 20, "----------------------------------");
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 1), 15, 230, 20, "总金额：" + _this.orderData.receivePayment +
                ' 实收金额：' + _this.orderData.actualPayment);
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 2), 15, 230, 20, '是否付款：' + (_this.orderData.isPayed === true ? '已付款' : '未付') +
                " 付款方式：" + (_this.orderData.payMethod === 'aliPay' ? '支付宝' : '其它'));
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 3), 15, 260, 20, "----------------------------------");
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 4), 15, 230, 20, "操作人：" + _this.orderData.creator);
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 5), 15, 230, 20, "顾客电话：" + _this.orderData.phone);
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 6), 15, 230, 20, "顾客名称：" + _this.orderData.name);
            LODOP.ADD_PRINT_TEXT(170 + 25 * (pointLength + 7), 15, 230, 20, "取衣时间：" + _this.orderData.pickingTime);// ADD_PRINT_TEXT(intTop,intLeft,intWidth,intHeight,strContent)增加纯文本项
            LODOP.SET_PRINT_STYLEA(0, "Stretch", 2);//按原图比例(不变形)缩放模式
            LODOP.PREVIEW();    //打印预览
            LODOP.PRINT();
            if (_this.dctPrintTimes.value >= 2) {
                this.$confirm('是否继续打印?', '提示', {
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                    type: 'warning'
                }).then(() => {
                    //请求
                    _this.printTicket()
                }).catch(() => {
                    _this.resetAll()
                });
            } else {
                _this.resetAll()
            }
        },
        changePrintTimes: function () {
            var _this = this;
            if (_this.orderData.printTimes !== null) {
                _this.dctPrintTimes.value = _this.orderData.printTimes;
            }
            var data = {
                method: 'put',
                url: context + '/dictionary',     //参数为common里的常量printTimes
                data: _this.dctPrintTimes,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    // _this.orderData.printTimes = data.data;
                    // _this.dctPrintTimes.value = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        getPrintTimes: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/dictionary/' + PRINTTIMES,     //参数为common里的常量printTimes
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.orderData.printTimes = data.data;
                    _this.dctPrintTimes.value =_this.orderData.printTimes ;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        //检查按钮权限
        showBtn: function (code) {
            var buttonArray = localStorage.getItem(BUTTON_CODES);
            return buttonArray.match(code);
        },
        //价格改变时，修改订单价格并格式化
        listPriceChange: function (row, price) {
            var length = this.hangingPointList.length;
            this.orderData.receivePayment = '';
            for (var first = 0; first < length; first++) {
                if (this.orderData.receivePayment === undefined ||
                    this.orderData.receivePayment === '' ||
                    this.orderData.receivePayment === null) {
                    this.orderData.receivePayment = this.hangingPointList[first].price;
                } else {
                    this.orderData.receivePayment = parseFloat(this.orderData.receivePayment) + parseFloat(this.hangingPointList[first].price);
                }
            }
            this.orderData.receivePayment.toString();
            this.priceFormat('receive');
            this.orderData.actualPayment = parseFloat(this.orderData.receivePayment) * parseFloat(this.customerInfo.discount);
            this.orderData.actualPayment.toString();
            this.priceFormat('actual');
        },
        //选中所有
        selectAll: function () {
            this.$refs.hangingPointListRef.clearSelection();
            this.toggleSelection(this.hangingPointList);
            this.listPhones();
            this.listCustomer();
        },
        priceChange: function () {
            this.priceFormat('actual');
        },
        //检查是否VIP
        checkVip: function () {
            var _this = this;
            _this.selectAll();
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + _this.orderData.phone,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined && data.data.nic !== '' && data.data.nic !== null) {
                        _this.$message.success("存在该会员");
                        //会员信息赋值
                        _this.customerInfo = data.data;
                        // if (_this.isCheckVip){
                        //     return
                        //     //会员信息变化
                        // }
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.orderData.actualPayment = _this.orderData.receivePayment * _this.customerInfo.discount;
                        _this.priceFormat('actual');
                        _this.twoDecimalCustomerInfoFormat('balance');
                        _this.isCheckVip = true;
                        _this.orderData.nic = _this.customerInfo.nic;
                    } else {
                        _this.isCheckVip = false;
                        _this.orderData.actualPayment = _this.orderData.receivePayment;
                        _this.$message.warning("不存在该会员");
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        changePhone:function(){
            this.orderData.actualPayment = this.orderData.receivePayment;
        },
        updatedVipAfterSub: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + _this.orderData.phone,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined && data.data.nic !== '' && data.data.nic !== null) {
                        //会员信息赋值
                        _this.customerInfo = data.data;
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.twoDecimalCustomerInfoFormat('balance');
                        _this.isCheckVip = true;
                        _this.orderData.nic = _this.customerInfo.nic;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        updatedVip: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + _this.orderData.phone,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined && data.data.nic !== '' && data.data.nic !== null) {
                        //会员信息赋值
                        _this.customerInfo = data.data;
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.orderData.actualPayment = _this.orderData.actualPayment * _this.customerInfo.discount;
                        _this.priceFormat('actual');
                        _this.twoDecimalCustomerInfoFormat('balance');
                        _this.isCheckVip = true;
                        _this.orderData.nic = _this.customerInfo.nic;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        //重置
        resetAll: function () {
            this.getDays();
            this.isCheckVip = false;
            this.orderData = {
                ordersId: '',
                receivePayment: '',
                actualPayment: '',
                name: '',
                phone: '',
                printTimes: this.dctPrintTimes.value,
                isPayed: false,
                nic: '',
                pickingTime: this.getLocalDateTime("yyyy-MM-dd HH:mm:ss"),  //当前时间+N天
                payMethod: 'aliPay',
                hangingPoints: [],
            };
            this.hangingPointList = [];
            this.query.key = '';
        },
        //结账
        submitList: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/hangingPoint/submit-orders',
                data: _this.orderData,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.$message.success("订单生成成功");
                    _this.orderData = data.data;
                    _this.hangingPointList = _this.orderData.hangingPoints;
                    _this.isQuery = true;
                    _this.customerInfo.discount=1;
                    _this.printTicket();
                } else {
                    _this.$message.warning(data.msg);
                }
            }).then(function () {
                if (_this.isCheckVip){
                    _this.updatedVipAfterSub();
                }
            });
        },

        //提交订单
        showPayPicture: function () {
            var _this = this;
            _this.$refs['orderDataRef'].validate((valid) => {
                if (valid) {
                    _this.orderData.isPayed=true;
                    if (_this.orderData.payMethod === 'aliPay') {
                        window.open(context + '/pay/' + _this.orderData.actualPayment, "_blank");
                    } else {
                        _this.$message.warning("请选择支付宝付款");
                    }
                } else {
                    _this.$message.warning("请填写必要信息");
                }
            });
        },
        onSubmit: function () {
            var _this = this;
            if (parseFloat(_this.customerInfo.balance)<parseFloat(_this.orderData.actualPayment)){
                _this.$message.warning("会员余额不足");
                return
            }
            _this.$refs['orderDataRef'].validate((valid) => {
                if (valid) {
                    _this.orderData.hangingPoints = _this.multipleSelection;
                    //选择了有效数据
                    if (_this.multipleSelection.length > 0) {
                        //提示是否选择支付
                        if (!_this.orderData.isPayed) {
                            this.$confirm('该订单未付款，是否继续?', '提示', {
                                confirmButtonText: '是',
                                cancelButtonText: '否',
                                type: 'warning'
                            }).then(() => {
                                //请求
                                _this.submitList()
                            }).catch(() => {
                            });
                        } else {
                            _this.submitList()
                        }
                    } else {
                        _this.$message.warning("请填写必要信息");
                    }
                }
            });
        },
        twoDecimalCustomerInfoFormat: function (str2) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (reg.test(this.customerInfo[str2])) {
                this.customerInfo[str2] = this.customerInfo[str2].toString().replace(reg, '$1.00');
            }
        },
        priceFormat: function (price) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (price === 'receive') {
                if (reg.test(this.orderData.receivePayment)) {
                    this.orderData.receivePayment = this.orderData.receivePayment.toString().replace(reg, '$1.00');
                }
            } else if (price === 'actual') {
                if (reg.test(this.orderData.actualPayment)) {
                    this.orderData.actualPayment = this.orderData.actualPayment.toString().replace(reg, '$1.00');
                    this.orderData.actualPayment = (parseFloat(this.orderData.actualPayment)).toFixed(2);
                }
            } else if (price === 'discount') {
                if (reg.test(this.customerInfo.discount)) {
                    this.customerInfo.discount = (this.customerInfo.discount).toString().replace(reg, '$1.0');
                }
            }
        },
        closeSetDay: function () {
            var _this = this;
            _this.setDaysDialogVisible = false;
            _this.getDays();
        },
        showEditDays: function () {
            var _this = this;
            _this.getDays();
            _this.setDaysDialogVisible = true;
        },
        getDays: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/dictionary/' + "packingTimeDays",
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.dictionary.value = data.data;
                    _this.orderData.pickingTime = _this.getLocalDateTime("yyyy-MM-dd HH:mm:ss", _this.dictionary.value); //当前时间+N天
                    //生成挂点号
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        editPackingTime: function () {
            var _this = this;
            var data = {
                method: 'put',
                url: context + '/dictionary',
                data: _this.dictionary,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.orderData.pickingTime = _this.getLocalDateTime("yyyy-MM-dd HH:mm:ss", _this.dictionary.value); //当前时间+N天
                    _this.closeSetDay();
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        listPhones: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/phones',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data === null) {
                        while (_this.reacquireTimes < 2) {
                            _this.reacquireTimes++;
                            this.listPhones();
                        }
                    } else {
                        _this.reacquireTimes = 0;
                        _this.phoneList = data.data;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        listCustomer: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/name-and-phone',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data === null) {
                        while (_this.reacquireTimes < 2) {
                            _this.reacquireTimes++;
                            this.listCustomer();
                        }
                    } else {
                        _this.reacquireTimes = 0;
                        _this.customerList = data.data;
                    }
                    //生成挂点号
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        //搜索功能的input
        querySearchSelect(item) {
            this.orderData.phone = item.phone;
        },
        setOrderDataName(item) {
            this.orderData.name = item.name;
        },

        queryNameSearch(queryString, cb) {
            var phones = this.customerList;
            var results = queryString ? phones.filter(this.createNameFilter(queryString)) : phones;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        createNameFilter(queryString) {
            return (phones) => {
                return (phones.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        queryPhoneSearch(queryString, cb) {
            var phoneList = this.phoneList;
            this.changePhone();
            var results = queryString ? phoneList.filter(this.createNameFilter(queryString)) : phoneList;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        getLocalDateTime: function (fmt, day) {
            if (day === undefined || day === null) {
                day = 3;
            }
            var myDate = new Date();
            myDate.setDate(myDate.getDate() + day);
            var o = {
                "M+": myDate.getMonth() + 1, //月份
                "d+": myDate.getDate() , //日
                "HH+": myDate.getHours(), //小时
                "m+": myDate.getMinutes(), //分
                "s+": myDate.getSeconds(), //秒
                "q+": Math.floor((myDate.getMonth() + 3) / 3), //季度
                "S": myDate.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (myDate.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        },

        //复选框
        handleSelectionChange: function (val) {
            this.multipleSelection = val;
        },
        toggleSelection: function (rows) {
            var _this = this;
            if (rows) {
                rows.forEach(row => {
                    if (_this.$refs["hangingPointListRef"] !== null) {
                        _this.$nextTick(function () {
                            this.$refs["hangingPointListRef"].toggleRowSelection(row);
                        })
                    }
                });
            } else {
                if (_this.$refs["hangingPointListRef"] !== null) {
                    _this.$nextTick(function () {
                        this.$refs.hangingPointListRef.clearSelection();
                    })
                }
            }
        },
        handleInputFocus: function (row, val) {
            this.toggleSelection();
            this.toggleSelection([row]);
            if (val === 'cloth') {
                this.handleSelect('1')
            } else if (val === 'color') {
                this.handleSelect('2')
            } else if (val === 'service') {
                this.handleSelect('3')
            } else if (val === 'brand') {
                this.handleSelect('4')
            }
            this.desFocus = false;
        },
        handleDesInputFocus: function () {
            this.desFocus = true;
        },

        removeByValue: function (arr, val) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] === val) {
                    arr.splice(i, 1);
                    break;
                }
            }
        },
        showDel: function (row) {
            this.removeByValue(this.hangingPointList, row);
            this.orderData.receivePayment -= row.price;
            this.orderData.actualPayment = this.orderData.receivePayment.toString();
            if (this.orderData.receivePayment !== 0) {
                if (this.customerInfo.discount === '1.0') {
                    this.orderData.actualPayment = (this.orderData.receivePayment).toFixed(2).toString();
                } else {
                    this.orderData.actualPayment = (this.orderData.receivePayment * this.customerInfo.discount + 0.5).toFixed(2).toString();
                }
            } else {
                this.orderData.actualPayment = (0).toFixed(2).toString();
            }
            this.priceFormat('actual');
            this.priceFormat('receive')
        },
        //生成挂点对象
        generateHangingPoint: function (cloth) {
            var _this = this;
            _this.hangingPoint.clothType = cloth.name;
            _this.hangingPoint.price = cloth.price;
            var data = {
                method: 'post',
                url: context + '/hangingPoint/generate-hanging-point',
                data: _this.hangingPoint,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.hangingPointList.push(data.data);
                    //清空页面复选
                    if (_this.$refs["hangingPointListRef"] !== null) {
                        _this.$nextTick(function () {
                            _this.$refs.hangingPointListRef.clearSelection();
                        })
                    }
                    //选中传入对象列
                    setTimeout(() => {
                        _this.toggleSelection([data.data]);
                    }, 50);
                    //选中这条数据的复选框
                    _this.multipleSelection.push(data.data);
                    //重置顶部菜单
                    _this.resetEMDBottomColor();
                    //改变顶部菜单底部选中颜色
                    var color = document.getElementById("color-id");
                    color.style = "border-bottom: 2px solid #409EFF;color: #303133;";
                    _this.menuKey = '2';
                    //请求颜色数据
                    _this.listColor();
                    //把价格加到应收位置
                    if (_this.orderData.receivePayment === undefined || _this.orderData.receivePayment === '') {
                        _this.orderData.receivePayment = '0';
                    }
                    _this.orderData.receivePayment = (parseFloat(_this.orderData.receivePayment) +
                        parseFloat(data.data.price)).toString();
                    _this.orderData.actualPayment = _this.orderData.receivePayment;
                    _this.priceFormat("receive");
                    _this.priceFormat("actual")
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        //添加衣物类型
        writeCloth: function (cloth) {
            var _this = this;
            //生成有挂点的对象
            _this.generateHangingPoint(cloth);

        },
        //品牌
        writeBrand: function (brand) {
            var _this = this;
            var cloth;
            if (_this.multipleSelection.length === 1) {
                _this.multipleSelection.forEach(row => {
                    row.brand = brand.name;
                    row.brandId = brand.id;
                    _this.resetEMDBottomColor();
                    cloth = document.getElementById("cloth-id");
                    cloth.style = "border-bottom: 2px solid #409EFF;color: #303133;";
                    _this.menuKey = '1';
                    _this.listClothParent();
                    _this.listCloth();
                });
            }
        },
        //重置顶部菜单底部颜色
        resetEMDBottomColor: function () {
            var cloth = document.getElementById("cloth-id");
            var brand = document.getElementById("brand-id");
            var service = document.getElementById("service-id");
            var color = document.getElementById("color-id");
            cloth.style = "border-bottom-color: transparent;color: #909399;";
            brand.style = "border-bottom-color: transparent;color: #909399;";
            service.style = "border-bottom-color: transparent;color: #909399;";
            color.style = "border-bottom-color: transparent;color: #909399;";
        },
        //服务类型
        writeService: function (service) {
            var _this = this;
            if (_this.multipleSelection.length === 1) {
                _this.multipleSelection.forEach(row => {
                    if (row.service === undefined || row.service === '' || row.service === null) {
                        row.service = service.name;
                    } else {
                        row.service += '，' + service.name;
                    }
                });
            }
        },
        //颜色
        writeColor: function (color) {
            var service;
            var _this = this;
            if (_this.multipleSelection.length === 1) {
                _this.multipleSelection.forEach(row => {
                    row.color = color.name;
                    row.colorId = color.id;
                    _this.resetEMDBottomColor();
                    service = document.getElementById("service-id");
                    service.style = "border-bottom: 2px solid #409EFF;color: #303133;";
                    _this.menuKey = '3';
                    _this.listService();

                });
            }
        },
        changeKey: function () {
            var _this = this;
            var key = _this.menuKey;
            _this.handleSelect(key)
        },
        handleSelect(key) {
            var _this = this;
            if (key !== null && key !== '') {
                _this.menuKey = key;
            }
            _this.handleSelectChangeBottomColor(key);
            if (key === '1') {
                _this.listClothParent();
                _this.listCloth();
            } else if (key === '2') {
                _this.listColor();
            } else if (key === '3') {
                _this.listService();
            } else if (key === '4') {
                _this.listBrand();
            }
        },
        handleSelectChangeBottomColor(key) {
            this.resetEMDBottomColor();
            if (key === '1') {
                var cloth = document.getElementById("cloth-id");
                cloth.style = "border-bottom: 2px solid #409EFF;color: #303133;";
            } else if (key === '2') {
                var color = document.getElementById("color-id");
                color.style = "border-bottom: 2px solid #409EFF;color: #303133;";
            } else if (key === '3') {
                var service = document.getElementById("service-id");
                service.style = "border-bottom: 2px solid #409EFF;color: #303133;";
            } else if (key === '4') {
                var brand = document.getElementById("brand-id");
                brand.style = "border-bottom: 2px solid #409EFF;color: #303133;";
            }
        },
        queryKey: function () {
            var _this = this;
            if (_this.menuKey === '1') {
                _this.listClothParent();
                _this.listCloth();
            } else if (_this.menuKey === '2') {
                _this.listColor();
            } else if (_this.menuKey === '3') {
                _this.listService();
            } else if (_this.menuKey === '4') {
                _this.listBrand();
            }
            _this.handleSelectChangeBottomColor(_this.menuKey);
        },
        handleSelectCloth(key, keyPath) {
            var _this = this;
            _this.query.id = key;
            _this.listCloth();
        },
        listClothParent: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/clothingType/parent-types',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.clothParentOptions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        checkClothParent: function () {
            var _this = this;
            if (_this.addClothParent !== undefined && _this.addClothParent !== '') {
                var data = {
                    method: 'post',
                    url: context + '/clothingType/check-parent/' + _this.addClothParent,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status !== 1) {
                        _this.$message.warning(data.msg);
                    } else {
                        _this.listClothParent();
                    }
                }).catch(function (error) {
                })
            }
        },
        listService: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/serviceType',
                params: _this.query,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.serviceList = data.data;
                    } else {
                        _this.serviceList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        listCloth: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/clothingType/list',
                data: _this.query,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.clothList = data.data;
                    } else {
                        _this.clothList = [];
                    }
                    _this.query.id = '';
                    // _this.query.key = '';
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        listBrand: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/brand',
                params: _this.query,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.brandList = data.data;
                    } else {
                        _this.brandList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        listColor: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/color',
                params: _this.query,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.colorList = data.data;
                    } else {
                        _this.colorList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
    }
});