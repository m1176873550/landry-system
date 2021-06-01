var vm = new Vue({
    el: '#hangingPoint',
    created() {
        var _this = this;
        // _this.list();
        // _this.laundryIdAndName();
    },
    mounted() {
        let _this = this;
        _this.getUrlValue();
        _this.brands();
        _this.colors();
        _this.serviceTypes();
        _this.clothTypes();
        //顾客手机号名字
        _this.listCustomer();
        _this.listPhones();
        //获取字典设置的待取衣天数
        _this.getDays();
    },
    updated: function () {
        this.$refs.hangingPointListRef.doLayout();
    },
    data: function () {
        let checkPhone = (rule, value, callback) => {
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
        var checkNumAndEnAndCn = (rule, value, callback) => {
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
        var checkPrice = (rule, value, callback) => {    // 验证价格
            if (value) {
                if (/^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/.test(value)) {
                    callback();
                } else {
                    callback(new Error('余额格式错误'));
                }
            } else {
                callback(new Error('余额不能为空'));
            }
        };
        return {
            query: {
                key: '',
                currentPage: 1,
                size: 10,
                order: 'desc',
                orderKey: 'createdAt',
                total: 0,
                number: '',
                orderId: '',
                isFinished: '',
                beanTook: 0,
                payer: '',
                phone: '',
                isBack: '',
                refund: '',
            },
            hangingPointList: [],
            myTableHeight: '85%',
            infoDialogVisible: false,       //查看
            saveDialogVisible: false,       //编辑新增窗口
            hangingPoints: [],
            hangingPoint: {
                id: '',
                isFinished: '',
                orderId:'',
                number: '',
                clothType: '',
                payer: '',
                phone: '',
                isBack: '',
                refund: '',
                refundAmount: '',
                beanTook: '',
                des: '',
                pickingTime2:new Date(),
                annexUrl: '',
                price: '',
                colorId: '',
                brandId: '',
                serviceTypeId: [],
                nic: '',                     //会员号
                serviceTypes: '',          //服务类型的集合拼接的字符串
                clothTypeId: '',
            },
            annexUrl: '',                     //附件
            imgVersion: 1,                   //图片请求次数
            addOrEdit: '',                   //新增还是编辑,查看
            addOrEditTitle: '',              //新增还是编辑标题
            isOptions: [
                {
                    id: 1,
                    name: '是',
                }, {
                    id: 0,
                    name: '否',
                },
            ],            //
            isOption: {},
            isRefund: false,
            colorOptions: [],
            colorOption: {},
            clothTypeOptions: [],
            clothTypeOption: {},
            serviceTypeOptions: [],
            serviceTypeOption: {},
            brandOptions: [],
            brandOption: {},
            customerList: [],                //顾客名称电话对象列表
            phoneList: [],                  //号码列表
            countDay: 3,                     //字典表计数天数
            reacquireTimes: 0,              //重复请求次数
            customerInfo: {
                fullName: '',
                balance: '',
                nic: '',
                discount: '1.0',
                actualPayment: '',           //实收金额
            },
            hangingPointType: '取衣类型',
            isCheckVip: false,           //是否VIP查询，展示VIP信息
            // formLabelWidth: '100px',       // 弹框表单元素宽度
            //是否付款弹框
            isPayedDialogVisible: false,
            isPayedInformation: '',
            isPayedObj: {
                id: '',
                price: '',
                isPay: '',
                payer: '',
                phone: '',
            },
            isPayed: false,              //是否支付
            isPayedObjRules: {
                isPay: [
                    {required: true, message: '是否付款不能为空', trigger: 'blur'},
                ],
                payer: [
                    {required: true, message: '付款人不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '付款人格式错误', trigger: 'change'},
                    {validator: checkNumAndEnAndCn, trigger: 'change'}
                ],
                price: [
                    {required: true, message: '付款金额不能为空', trigger: 'change'},
                    {validator: checkPrice, message: '付款金额格式错误', trigger: 'change'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'change'}
                ],
            },
            rules: {
                clothTypeId: [
                    {required: true, message: '付款人不能为空', trigger: 'blur'},
                ],
                payer: [
                    {required: true, message: '付款人不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '付款人格式错误', trigger: 'change'},
                    {validator: checkNumAndEnAndCn, trigger: 'change'}
                ],
                price: [
                    {required: true, message: '金额不能为空', trigger: 'change'},
                    // {validator: checkCount, message: '金额格式错误', trigger: 'blur'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'change'}
                ],
                description: [
                    {max: 100, message: '挂点描述格式错误', trigger: 'change'}
                ]
            },
        }
    },
    methods: {
        //检查按钮权限
        showBtn: function (code) {
            var buttonArray = localStorage.getItem(BUTTON_CODES);
            return buttonArray.match(code);
        },
        handleDropDown: function (command) {
            var _this = this;
            switch (command) {
                case '0':
                    _this.query.beanTook = '';
                    _this.list();
                    _this.hangingPointType = '全部';
                    break;
                case '1':
                    _this.query.beanTook = 1;
                    _this.list();
                    _this.hangingPointType = '已取';
                    break;
                case '2':
                    _this.query.beanTook = 0;
                    _this.list();
                    _this.hangingPointType = '未取';
                    break;

            }
        },
        //取衣弹框
        showTakeCloth: function (row) {
            this.isPayed = row.isPay === 1;
            this.isPayedObj.id = row.id;
            this.isPayedObj.payer = row.payer;
            this.isPayedObj.phone = row.phone;
            this.isPayedObj.isPay = row.isPay === 1 ? '是' : '否';
            this.isPayedObj.price = row.price;
            this.isPayedDialogVisible = true;
        },
        changeIsPay: function () {
            var _this = this;
            var data = {
                method: 'put',
                url: context + '/hangingPoint/change-is-payed',
                data: _this.isPayedObj,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.isPayedObj.isPay = '是';
                    _this.isPayed = true;
                    _this.list();
                    _this.$message.success('付款成功');
                    //生成挂点号
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("changeIsPay error");
            })
        },
        changeBeanTook: function () {
            var _this = this;
            if (_this.isPayedObj.isPay === '否') {
                _this.$message.warning("当前衣物还未付款，请付款");
                return;
            }
            var data = {
                method: 'put',
                url: context + '/hangingPoint/' + _this.isPayedObj.id + '/change-bean-took',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.$message.success("取衣成功");
                    _this.list();
                    _this.isPayedChance();
                    //生成挂点号
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("changeBeanTook error");
            })
        },
        isPayedChance() {
            this.isPayedDialogVisible = false;
        },
        isPayedClose: function (done) {
            done();
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};

            this.resetFormValue('isPayedObjRef');
            this.saveDialogVisible = false;
        },
        //取衣弹框
        //选中所有
        selectAll: function () {
            this.listPhones();
            this.listCustomer();
        },
        //数据库取衣天数时间
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
                    _this.countDay = data.data;
                    _this.hangingPoint.pickingTime2 = _this.getLocalDateTime("yyyy-MM-dd HH:mm:ss", _this.countDay); //当前时间+N天
                    //生成挂点号
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("getDays error");
            })
        },
        formatLocalDateTime: function (fmt, myDate) {
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
        //生成时间
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
        //查询会员
        checkVip: function () {
            var _this = this;
            if (_this.hangingPoint.clothTypeId === '') {
                _this.$message.warning("请选择衣物类型");
                return;
            } else if (_this.hangingPoint.price === '') {
                _this.$message.warning("请输入付款金额");
                return;
            } else if (_this.hangingPoint.payer === '') {
                _this.$message.warning("请输入顾客姓名");
                return;
            }
            _this.selectAll();
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + _this.hangingPoint.phone,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined && data.data.nic !== null && data.data.nic !=='') {
                        _this.$message.success("存在该会员");
                        //会员信息赋值
                        _this.customerInfo = data.data;
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.customerInfo.actualPayment = _this.hangingPoint.price * _this.customerInfo.discount;
                        // _this.priceFormat('actual');
                        _this.twoDecimalCustomerInfoFormat('balance');
                        _this.hangingPoint.nic = _this.customerInfo.nic;
                        _this.isCheckVip = true;
                    } else {
                        _this.$message.warning("不存在该会员");
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("onSubmit error");
            })
        },
        //两位小数格式化
        twoDecimalCustomerInfoFormat: function (str2) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (reg.test(this.customerInfo[str2])) {
                this.customerInfo[str2] = this.customerInfo[str2].toString().replace(reg, '$1.00');
                // if (!regTwo.test(this.customerInfo["str2"])) {
                //     this.customerInfo[str2] = (this.customerInfo[str2] + 0.005).toFixed(2).toString();
                // }
            }
        },
        //价格格式化
        priceFormat: function (price) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (price === 'discount') {
                if (reg.test(this.customerInfo.discount)) {
                    this.customerInfo.discount = (this.customerInfo.discount).toString().replace(reg, '$1.0');
                }
            }
        },
        getUrlValue: function () {
            let _this = this;
            var value = window.parent.location.href.split("orderId=")[1];
            if (value !== '' && value !== undefined) {
                //查询字段赋值
                _this.query.orderId = value.split("phone=")[0].split('&')[0];
                console.log(_this.query.orderId);
                _this.query.phone = value.split("phone=")[1].split('&')[0];
                console.log(_this.query.phone);
                _this.query.number = value.split("number=")[1];
                if (_this.query.number === '?') {
                    _this.query.number = '';
                }
                console.log(_this.query.number);
                _this.list();
            } else {
                _this.list();
            }
        },
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "isFinished") {
                _this.query.orderKey = "isFinished";
            } else if (column.prop === "parentId") {
                _this.query.orderKey = "parentId";
            } else if (column.prop === "number") {
                _this.query.orderKey = "number";
            } else if (column.prop === "price") {
                _this.query.orderKey = "price";
            } else if (column.prop === "discount") {
                _this.query.orderKey = "discount";
            } else if (column.prop === "isBack") {
                _this.query.orderKey = "isBack";
            } else if (column.prop === "refund") {
                _this.query.orderKey = "refund";
            } else if (column.prop === "refundAmount") {
                _this.query.orderKey = "refundAmount";
            } else if (column.prop === "pickingTime2") {
                _this.query.orderKey = "pickingTime2";
            } else if (column.prop === "updatedAt") {
                _this.query.orderKey = "updatedAt";
            }
            if (column.order === DESC) {
                _this.query.order = 'desc'
            } else if (column.order === ASC) {
                _this.query.order = 'asc'
            }
            _this.list();
        },
        typeChange: function (value) {
            let _this = this;
            if (value.length !== 0) {
                _this.hangingPoint.clothType = value[value.length - 1];
                _this.hangingPoint.clothTypeId = value[value.length - 1];
                _this.changePrice(_this.hangingPoint.clothType);
            }
            _this.$refs.clothTypeRef.dropDownVisible = false;
        },
        changePrice: function (id) {
            let _this = this;
            let data = {
                method: 'get',
                url: context + '/clothingType/' + id,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.hangingPoint.price = data.data;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("price error");
            })
        },
        brands: function () {
            let _this = this;
            let data = {
                method: 'get',
                url: context + '/brand',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.brandOptions = data.data;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("brandList error");
            })
        },
        serviceTypes: function () {
            let _this = this;
            let data = {
                method: 'get',
                url: context + '/serviceType',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.serviceTypeOptions = data.data;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("serviceList error");
            })
        },
        clothTypes: function () {
            let _this = this;
            let data = {
                method: 'get',
                url: context + "/clothingType/group",
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    _this.clothTypeOptions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("clothadd error");
            })
        },
        colors: function () {
            let _this = this;
            let data = {
                method: 'get',
                url: context + "/color",
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    _this.colorOptions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("clothadd error");
            })
        },
        handleImgError: function (e) {
            if (this.annexUrl !== '' && this.annexUrl !== undefined) {
                if (this.imgVersion <= 3) {
                    this.annexUrl = this.annexUrl + "&version=" + (this.imgVersion++);
                } else {
                    console.log(e);
                }
            }
        },
        deleteRow: function (id) {
            let _this = this;
            let data = {
                method: 'delete',
                url: context + '/hangingPoint/' + id
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    _this.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    _this.list();
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("del");
            })
        },
        showDel(row) {
            let _this = this;
            _this.$confirm('此操作将删除该挂点, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                _this.deleteRow(row.id);
            });
        },
        saveEdit: function () {             //新增 编辑
            let _this = this;
            var serviceTypes = _this.hangingPoint.serviceTypeId;
            for (var count = 0; count < serviceTypes.length; count++) {
                if (_this.hangingPoint.serviceTypes !== '' && _this.hangingPoint.serviceTypes.length > 0) {
                    _this.hangingPoint.serviceTypes += "," + serviceTypes[count]
                } else {
                    _this.hangingPoint.serviceTypes += serviceTypes[count]
                }
            }
            //如果是会员
            if (_this.isCheckVip) {
                _this.hangingPoint.price = _this.customerInfo.actualPayment;
            }
            _this.hangingPoint.serviceTypes.toString();
            _this.$refs['hangingPointRef'].validate((valid) => {
                if (valid) {
                    let data;
                    if (_this.addOrEdit === 'edit') {
                        // _this.hangingPoint.pickingTime2=_this.formatLocalDateTime("yyyy-MM-dd HH:mm:ss", _this.hangingPoint.pickingTime2);
                        data = {
                            method: 'put',
                            url: context + '/hangingPoint',
                            data: _this.hangingPoint,
                        };
                    } else if (_this.addOrEdit === 'add') {
                        data = {
                            method: 'post',
                            url: context + '/hangingPoint/add',
                            data: _this.hangingPoint,
                        };
                    } else {
                        return false;
                    }
                    axios(data).then(function (rs) {
                        // if (rs.data===403){
                        //     window.parent.location.href="/view/login/login.htm";
                        // }
                        let data = rs.data;
                        if (data.status === 1) {
                            _this.hangingPoint.serviceTypes = '';
                            _this.resetFormValue();
                            _this.list();
                            _this.chanceClose();
                            _this.$message.success("添加或修改成功");
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {
                        console.log("saveEdit");
                    })
                }
            });
        },
        resetFormValue: function (formName) {
            let _this = this;
            if (_this.$refs[formName] != null) {
                _this.$nextTick(function () {
                    _this.$refs[formName].resetFields();
                })
            }
        },
        showEdit: function (row) {
            let _this = this;
            _this.listToHangingPoint(row);
            _this.addOrEdit = 'edit';
            _this.addOrEditTitle = "编辑挂点";
            _this.saveDialogVisible = true;
            _this.isRefund = row.refund === 0;
        },
        isBackChange: function () {
            if (this.hangingPoint.refund === 0) {
                this.isRefund = true;
            } else {
                this.isRefund = false;
            }
        },
        showAdd: function () {
            let _this = this;
            _this.getDays();
            _this.addOrEdit = 'add';
            _this.addOrEditTitle = "新增挂点";
            // _this.resetFormValue('hangingPointRef');
            _this.isRefund = true;                   //退款金额不可编辑
            //手动重置
            _this.resetHangingPointValue();
            _this.saveDialogVisible = true;
        },
        resetHangingPointValue: function () {
            let _this = this;
            _this.hangingPoint.id = '';
            _this.hangingPoint.isFinished = 0;
            _this.hangingPoint.beanTook = 0;
            _this.hangingPoint.annexUrl = '';
            _this.hangingPoint.price = '';
            _this.hangingPoint.phone = '';
            _this.hangingPoint.number = '';
            _this.hangingPoint.clothType = '';
            _this.hangingPoint.payer = '';
            _this.hangingPoint.des = '';
            _this.hangingPoint.refund = 0;
            _this.hangingPoint.refundAmount = '';
            _this.hangingPoint.pickingTime2 = '';
            _this.hangingPoint.isBack = 0;
            _this.hangingPoint.brandId = '';
            _this.hangingPoint.colorId = '';
            _this.hangingPoint.clothTypeId = '';
            _this.hangingPoint.serviceTypeId = [];
            _this.hangingPoint.serviceTypes = '';
            _this.hangingPoint.ordersId = '';
        },
        listToHangingPoint: function (row) {
            let _this = this;
            _this.hangingPoint.id = row.id;
            _this.hangingPoint.isFinished = row.isFinished;
            _this.hangingPoint.beanTook = row.beanTook;
            _this.hangingPoint.price = row.price;
            _this.hangingPoint.annexUrl = row.annexUrl === null ? '' : row.annexUrl;
            _this.hangingPoint.phone = row.phone;
            _this.hangingPoint.number = row.number;
            _this.hangingPoint.payer = row.payer;
            _this.hangingPoint.des = row.des;
            _this.hangingPoint.refund = row.refund;
            _this.hangingPoint.refundAmount = row.refundAmount;
            _this.hangingPoint.pickingTime2 = row.pickingTime2 === null ? '' : row.pickingTime2;
            _this.hangingPoint.isBack = row.isBack;
            _this.hangingPoint.brandId = row.brandId;
            _this.hangingPoint.colorId = row.colorId;
            _this.hangingPoint.clothTypeId = row.clothTypeId;
            _this.hangingPoint.orderId = row.orderId;

        },
        showInfo: function (row) {
            this.annexUrl = row.annexUrl;
            this.infoDialogVisible = true;
        },
        closeInfo() {
            this.annexUrl = '';
            this.infoDialogVisible = false;
        },
        addOrEditClose: function (done) {
            done();
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.isCheckVip = false;
            this.resetHangingPointValue();
            // this.resetFormValue('hangingPointRef');
            this.saveDialogVisible = false;
        },
        chanceClose: function () {
            this.resetHangingPointValue();
            // this.resetFormValue('hangingPointRef');
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.isCheckVip = false;
            this.saveDialogVisible = false;
        },
        isBackFormatter: function (row, column, cellValue, index) {
            if (row.isBack === 1) {
                return "是";
            } else if (row.isBack === 0) {
                return "否";
            } else {
                return;
            }
        },
        refundFormatter: function (row, column, cellValue, index) {
            if (row.refund === 1) {
                return "是";
            } else if (row.refund === 0) {
                return "否";
            } else {
                return;
            }
        },
        isFinishedFormatter: function (row, column, cellValue, index) {
            if (row.isFinished === 1) {
                return "是";
            } else if (row.isFinished === 0) {
                return "否";
            } else {
                return;
            }
        },
        isBeanTookFormatter: function (row, column, cellValue, index) {
            if (row.beanTook === 1) {
                return "是";
            } else if (row.beanTook === 0) {
                return "否";
            } else {
                return;
            }
        },
        isPayFormatter: function (row, column, cellValue, index) {
            if (row.isPay === 1) {
                return "是";
            } else if (row.isPay === 0) {
                return "否";
            } else {
                return;
            }
        },
        list: function () {
            let _this = this;
            let data = {
                method: 'post',
                url: context + '/hangingPoint',
                data: _this.query,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                let data = rs.data;
                if (data.status === 1) {
                    //重置父级地址
                    window.parent.history.replaceState(null, '', '/view/home/home.htm');
                    // window.parent.history.pushState({}, null,
                    //     window.parent.location.href.toString().split('?')[0]);
                    //结果赋值
                    _this.query.total = data.data.total;
                    _this.query.currentPage = data.data.current;
                    _this.query.size = data.data.size;
                    if (_this.query.total > 0) {
                        _this.hangingPointList = data.data.records;
                    } else {
                        _this.hangingPointList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {

            })
        },
        //用户手机号姓名
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
                console.log("listPhones error");
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
                console.log("listCustomer error");
            })
        },
        //搜索功能的input
        querySearchSelect(item) {
            this.hangingPoint.phone = item.phone;
        },
        setOrderDataName(item) {
            this.hangingPoint.payer = item.name;
        },
        handleIconClick(ev) {
            console.log(ev + "ev");
        },
        queryNameSearch(queryString, cb) {
            var phones = this.customerList;
            console.log(phones);
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
            var results = queryString ? phoneList.filter(this.createNameFilter(queryString)) : phoneList;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        //手机号姓名
        handleSizeChange: function (value) {
            let _this = this;
            _this.query.size = value;
            _this.list();

        },
        handleCurrentChange: function (value) {
            let _this = this;
            _this.query.currentPage = value;
            _this.list();
        },
    }
});