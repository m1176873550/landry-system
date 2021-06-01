var vm = new Vue({
    el: '#screen',
    created() {
        var _this = this;
        let urlArr=window.location.href.split("?id=");
        if (urlArr.length>1){
            let id=urlArr[1];
            _this.getUrlById(id);
        }
    },
    mounted() {
        var _this = this;
        _this.getUrlList();
        _this.getUsername();
        // document.getElementsByClassName("url-change").html('');
        // urlChange;
        // document.getElementById("url-change").load('../main/main.jsp');
        // _this.getUrl();
    },
    updated: function () {
        var _this = this;

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
        var checkRechargeAmount = (rule, value, callback) => {    // 充值金额预算
            if (value) {
                if (/^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/.test(value)) {
                    callback();
                } else {
                    callback(new Error('充值金额格式错误'));
                }
            } else {
                callback(new Error('充值金额不能为空'));
            }
        };
        var checkDiscount = (rule, value, callback) => {    // 验证折扣
            if (value) {
                if (/^([01](\.0+)?|0\.[0-9]+)$/.test(value)) {
                    callback();
                } else {
                    callback(new Error('折扣格式错误'));
                }
            } else {
                callback(new Error('折扣不能为空'));
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('新密码不能为空'));
            } else {
                if (this.updatePassword.repassword !== '') {
                    this.$refs.updatePassword.validateField('repassword');
                }
                var reg = /^[0-9A-Za-z]{6,20}$/;
                if (reg.test(value)) {
                    callback();
                } else {
                    callback(new Error('新密码格式错误'));
                }
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('重复新密码不能为空'));
            } else if (value !== this.updatePassword.password) {
                callback(new Error('重复新密码与新密码内容不一致'));
            } else {
                var reg = /^[0-9A-Za-z]{6,20}$/;
                if (reg.test(value)) {
                    callback();
                } else {
                    callback(new Error('重复新密码格式错误'));
                }
            }
        };
        return {
            query: {             //搜索框字段
                orderId: '',
                phone: '',
                number: '',
            },
            urlOptions: [            //路径对象列表
                // {name:'权限1',url:'123'},
                // {name:'权限2',url:'345'}
            ],
            urlOption: {},
            //1-3
            // brand
            query13: {           //1-3的查询对象
                key: "",
            },
            brandList: [],       //列表
            b: {
                id: '',
                name: '',
            },                   //新增衣物的对象
            brandDialogVisible: false,
            addBrandName: '',
            toBAdd: true,                        //是否展示品牌添加
            openingBAdd: false,                    //是否关闭品牌添加
            // brand
            //color
            colorList: [],           //颜色列表
            color: {
                id: '',
                name: '',
            },               //单个颜色对象
            colorDialogVisible: false,       //颜色弹框
            addColorName: '',
            toCAdd: true,            //展示添加
            openingCAdd: false,      //关闭添加
            //color
            //service
            serviceList: [],           //服务列表
            service: {
                id: '',
                name: '',
            },               //单个服务类型对象
            serviceDialogVisible: false,       //服务类型弹框
            addServiceName: '',
            toSAdd: true,            //展示添加
            openingSAdd: false,      //关闭添加
            //service
            //cloth
            clothList: [],           //服务列表
            cloth: {
                id: '',
                name: '',
                price: '',
                parentId: '',//添加衣物父类名
                parentName: '',//添加衣物父类名
            },               //单个服务类型对象
            clothDialogVisible: false,       //服务类型弹框
            addClothName: '',
            addClothParent: [],
            addClothPrice: '',
            toClothAdd: true,                //展示添加
            openingClothAdd: false,          //关闭添加
            clothParentOptions: [],
            clothParentOption: {},
            //cloth
            myTableHeight: '70%',
            dialogWidth: '43%',                 //弹窗宽度
            vipQueryDialogVisible: false,        //会员窗口显示
            customer: {
                id: '',
                laundryId: '',
                fullName: '',
                address: '',
                phone: '',
                des: '',
                discount: '1.0',
                balance: '0.00',
                nic: '',
                birthday: '',
                rechargeAmount: '',
            },
            addVip: false,                   //是否新增VIP
            queryVip: {
                phone: '',
            },
            //是否会员
            notVip:true,
            title:'',
            //时间
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
            },
            customerList: [],            //顾客名称手机号列表
            reacquireTimes: 0,            //重复接口次数
            reacquireUrlTimes: 0,            //地址重复请求次数
            phoneList: [],               //手机号名称列表
            customerRules: {
                fullName: [
                    {required: true, message: '会员姓名不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '会员姓名格式错误', trigger: 'blur'},
                    {validator: checkName, message: '会员姓名格式错误', trigger: 'change'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'change'}
                ],
                des: [
                    {max: 100, message: '会员描述格式错误', trigger: 'change'}
                ],
                balance: [
                    {required: true, message: '会员余额不能为空', trigger: 'blur'},
                    {validator: checkPrice, message: '会员余额格式错误', trigger: 'change'}
                ],
                rechargeAmount: [
                    // {required: true, message: '充值金额不能为空', trigger: 'change'},
                    {validator: checkRechargeAmount, message: '充值金额格式错误', trigger: 'blur'}
                ],
                discount: [
                    {validator: checkDiscount, message: '折扣格式错误(如0.00-1.00)', trigger: 'change'}
                ],
                nic: [
                    {required: true, message: '会员编号不能为空', trigger: 'blur'},

                ],
            },
            rules: {
                password: [
                    {validator: validatePass, trigger: 'blur'},
                    {required: true, message: '新密码不能为空', trigger: 'blur'},
                ],
                newPassword: [
                    {required: true, message: '重复新密码不能为空', trigger: 'blur'},
                    {validator: validatePass2, trigger: 'blur'}
                ]
            },
            //1-3

            // activeIndex:'1',
            pageContent: '/view/hanging-point/hanging-point-list.htm',
            urlId:'',
            username:'',
            dialogFormVisible:false,
            updatePassword: {
                name:'',
                password: '',
                newPassword: ''
            },

        }
    },
    methods: {
        editPassword: function () {
            var _this = this;
            _this.updatePassword.name=_this.username;
            _this.$refs['updatePassword'].validate((valid) => {
                if (valid) {
                    var data = {
                        method: 'put',
                        url: context + '/employee/doChangePwd',
                        data: _this.updatePassword
                    };
                    axios(data).then(function (rs) {
                        if (rs.data === 403) {
                            window.parent.location.href = "/view/login/login.htm";
                        }
                        var data = rs.data;
                        if (data.status === 1) {
                            _this.$message.success("修改成功");
                            _this.dialogFormVisible = false;
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {

                    });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetForm: function () {
            this.$refs['updatePassword'].resetFields();
        },
        handleUpdatePwdClose: function () {
            this.$refs.updatePassword.resetFields();
            this.dialogFormVisible = false;
        },
        handleUsernameDown:function(command){
            switch (command) {
                case '1':
                    this.dialogFormVisible = true;
                    this.updatePassword.password='';
                    this.updatePassword.newPassword='';
                    break;
                case '2':
                    this.logout();
                    break;
            }
        },
        getUsername: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/employee/getCurrentUserName',
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    _this.username=data.data;
                }
            }).catch(function (error) {
            })
        },
        logout: function () {
            var _this = this;
            var data = {
                method: 'put',
                url: context + "/employee/logout",
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    _this.$message.success("注销成功");
                    window.location.href = "/view/login/login.htm";
                }
            }).catch(function (error) {
                console.log("logout error");
            })
        },
        getUrlList: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/permission/url-name",
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    if (data.data.records === null) {
                        while (_this.reacquireUrlTimes < 2) {
                            _this.reacquireUrlTimes++;
                            _this.getUrlList();
                        }
                    } else {
                        _this.urlOptions = data.data;
                    }
                }
            }).catch(function (error) {
                console.log("getUrlList error");
            })
        },
        getUrlById: function (id) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/permission/getUrlById",
                params:{
                    id:id
                }
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    if (data.data.records === null) {
                        while (_this.reacquireUrlTimes < 2) {
                            _this.reacquireUrlTimes++;
                            this.getUrlById(id);
                        }
                    } else {
                        _this.pageContent = data.data;
                    }
                }
            })
        },
        getIdByUrl: function (url) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/permission/getIdByUrl",
                params:{url:url}
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    if (data.data.records === null) {
                        while (_this.reacquireUrlTimes < 2) {
                            _this.reacquireUrlTimes++;
                            this.getIdByUrl();
                        }
                    } else {
                        _this.urlId = data.data;
                    }
                }
            }).then(function (rs2) {
                let urlArr=window.location.href.split("?id=");
                window.history.replaceState(null,'',urlArr[0]+'?id='+_this.urlId);
            })
        },
        handleUrlOpen: function (key, keyPath) {
            this.getIdByUrl(key);
            if (key === '/view/login/login.htm'){
                window.location.href = "/view/login/login.htm";
            }else {
                this.pageContent = key;
            }
        },
        customerRecharge: function () {
            var _this = this;
            if (_this.customer.rechargeAmount ==='0.00'){
                return
            }
            _this.$refs['customerRef'].validate((valid) => {
                if (valid) {
                    var data;
                    data = {
                        method: 'put',
                        url: context + '/customer/recharge',
                        data: _this.customer,
                    };
                    axios(data).then(function (rs) {
                        // if (rs.data===403){
                        //     window.parent.location.href="/view/login/login.htm";
                        // }
                        var data = rs.data;
                        if (data.status === 1) {
                            _this.$message.success("充值成功");
                            _this.checkVip();
                            //刷新充值金额
                            _this.customer.rechargeAmount = '0.00';
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {
                        console.log("edit or add Info");
                    })
                }
            });
        },
        queryPhone: function () {

        },
        changePhone: function () {

        },
        customerInit: function () {
            this.customer.id = '';
            this.customer.fullName = '';
            this.customer.address = '';
            this.customer.phone = '';
            this.customer.des = '';
            this.customer.discount = '1.0';
            this.customer.balance = '0.00';
            this.customer.nic = '';
            this.customer.birthday = '';
            this.customer.rechargeAmount = '0.00';
        },
        queryCustomer() {
            var _this = this;
            if (!/^[1]([3-9])[0-9]{9}$/.test(_this.queryVip.phone)) {
                _this.$message.warning('查询手机号格式错误');
            } else {
                var data = {
                    method: 'get',
                    url: context + '/customer/checkPhone/' + _this.queryVip.phone,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        if (data.data === null || data.data === undefined) {
                            _this.$message.warning("不存在该会员");
                            _this.customerInit();
                        } else {
                            _this.customer = data.data;
                            _this.customer.rechargeAmount = '0.00';
                            // _this.$message.success("存在该会员");
                            _this.priceFormat('discount');
                            //计算折扣价格
                            _this.twoDecimalCustomerInfoFormat('balance');
                            _this.addVip = false;
                            // _this.twoDecimalCustomerInfoFormat('rechargeAmount');
                        }
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console.log("queryCustomer error");
                });
            }

        },
        setCustomerValue: function (item) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + item.value,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined) {
                        // _this.$message.warning("存在该会员");
                        _this.customer = data.data;
                        _this.addVip = false;
                        _this.customer.rechargeAmount = '0.00';
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.twoDecimalCustomerInfoFormat('balance');
                        // _this.twoDecimalCustomerInfoFormat('rechargeAmount');
                    } else {
                        _this.$message.warning("不存在该会员");
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("setCustomerValue error");
            });
        },
        setCustomerName(item) {
            this.customer.fullName = item.name;
        },
        addVipToTrue: function () {
            // this.resetFormValue('customerRef');
            this.customerInit();
            this.customer.balance = '0.00';
            this.title = '会员发行';
            this.customer.rechargeAmount = '0.00';
            this.priceFormat('discount');
            //计算折扣价格
            // this.twoDecimalCustomerInfoFormat('balance');
            // this.twoDecimalCustomerInfoFormat('rechargeAmount');
            this.addVip = true;
            this.notVip = true;
        },
        listVipPhones: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/phones/' + "vip",
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
                            this.listVipPhones();
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
        //拉去手机号名字提示列表
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
        createNameFilter(queryString) {
            return (phones) => {
                return (phones.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        queryPhoneSearch(queryString, cb) {
            var phoneList = this.phoneList;
            // console.log(phoneList);
            var results = queryString ? phoneList.filter(this.createNameFilter(queryString)) : phoneList;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        queryNameSearch(queryString, cb) {
            var phones = this.customerList;
            // console.log(phones);
            var results = queryString ? phones.filter(this.createNameFilter(queryString)) : phones;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        setCustomerPhone(item) {
            this.customer.phone = item.phone;
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
        //检查是否VIP
        checkVip: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/checkPhone/' + _this.customer.phone,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data !== undefined && data.data.nic !==null && data.data.nic !=='') {
                        // _this.$message.success("存在该会员");
                        //会员信息赋值
                        _this.customer = data.data;
                        _this.customer.rechargeAmount = '0.00';
                        _this.priceFormat('discount');
                        //计算折扣价格
                        _this.twoDecimalCustomerInfoFormat('balance');
                        // _this.twoDecimalCustomerInfoFormat('rechargeAmount');
                        _this.isCheckVip = true;
                        _this.notVip = false;
                    } else {
                        _this.$message.warning("不存在该会员");
                        _this.customer.nic = '';
                        _this.customer.discount = '1.0';
                        _this.customer.balance = '0.00';
                        _this.customer.rechargeAmount = '0.00';
                        _this.customer.des = '';
                        _this.customer.address = '';
                        _this.customer.birthday = '';
                        _this.notVip = true;
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("checkVip error");
            })
        },
        //格式化价格
        twoDecimalCustomerInfoFormat: function (str2) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (reg.test(this.customer[str2])) {
                this.customer[str2] = this.customer[str2].toString().replace(reg, '$1.00');
                console.log(this.customer[str2]);
            }
        },
        priceFormat: function (price) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (price === 'discount') {
                if (reg.test(this.customer.discount)) {
                    this.customer.discount = (this.customer.discount).toString().replace(reg, '$1.0');
                }
            }
        },
        //生成会员编号
        generateNic: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer/generate-nic'
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.customer.nic = data.data;
                    _this.customer.rechargeAmount = '0.00';
                    _this.customer.balance = '0.00';
                    _this.priceFormat('discount');
                    //计算折扣价格
                    // _this.twoDecimalCustomerInfoFormat('balance');
                    // _this.twoDecimalCustomerInfoFormat('rechargeAmount');
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("generateNic");
            })
        },
        clearQuery: function () {
            this.query.phone = '';
            this.query.number = '';
            this.query.orderId = '';
        },
        queryList: function () {
            var _this = this;
            if (_this.pageContent === '/view/hanging-point/hanging-point-list.htm') {
                document.getElementById('iframe').src = '/view/hanging-point/hanging-point-list.htm';
            }
            _this.pageContent = '/view/hanging-point/hanging-point-list.htm';
            window.history.pushState({}, null, location.href.toString() + '?orderId='
                + _this.query.orderId + '&phone=' + _this.query.phone + '&number=' + _this.query.number);
        },
        //iframe跳转
        handleTopSelect: function (key) {
            var _this = this;
            if (key !== null && key !== '') {
                switch (key) {
                    case '1-1':
                        _this.pageContent = '/view/hanging-point/hanging-point-list.htm';
                        break;
                    case '1-2':
                        _this.pageContent = '/view/employee/employee-list.htm';
                        break;
                    case '1-3-1':
                        _this.showCloth();
                        break;
                    case '1-3-2':
                        _this.showService();
                        break;
                    case '1-3-3':
                        _this.showColor();
                        break;
                    case '1-3-4':
                        _this.showBrand();
                        break;
                    case '2-1':
                        _this.pageContent = '/view/customer/customer-list.htm';
                        break;
                    case '2-2':
                        _this.title = '会员充值';
                        _this.vipQueryDialogVisible = true;
                        break;
                    case '2-3':
                        _this.addVipToTrue();
                        _this.vipQueryDialogVisible = true;
                        break;
                    case '5':
                        var queryChange = document.getElementById("query-change");
                        queryChange.style =
                            "color:rgb(255, 255, 255);background-color:rgb(84, 92, 100);border-bottom: 0 solid #545c64;color:#303133;";
                        break;
                }
            }
        },
        //1-3
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
                console.log("clothingTypeParent error");
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
                    console.log("clothingTypeParent error");
                })
            }
        },
        showService: function () {
            this.listService();
            this.serviceDialogVisible = true;
        },
        serviceClose: function () {
            this.serviceDialogVisible = false;
            this.addServiceName = '';
            this.toSAdd = true;            //展示添加按钮
            this.openingSAdd = false;      //关闭添加按钮
        },
        toAddService: function () {
            this.openingSAdd = true;      //关闭添加按钮，展示输入框
            this.toSAdd = false;
        },
        addService: function () {
            var _this = this;
            _this.service.name = _this.addServiceName;
            var data = {
                method: 'post',
                url: context + '/serviceType/',
                data: _this.service,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.query13.key = '';
                    _this.listService();
                    _this.$message.success("服务类型添加成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("serviceadd error");
            })
        },
        saveService: function (service) {
            var _this = this;
            var data;
            if (service.name === '' || service.name === undefined) {  // 删除
                data = {
                    method: 'delete',
                    url: context + '/serviceType/' + service.id,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listService();
                        _this.$message.success("服务类型删除成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("servicedelete error");
                })
            } else {                                         // 编辑
                // _this.brand.id = brand.id;
                // _this.brand.name = brand.name;
                data = {
                    method: 'put',
                    url: context + '/serviceType',
                    data: service,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listService();
                        _this.$message.success("服务类型修改成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("editservice error");
                })
            }
        },
        listService: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/serviceType',
                params: _this.query13,
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
                console.log("serviceList error");
            })
        },
        //衣物
        showCloth: function () {
            this.listClothParent();
            this.listCloth();
            this.clothDialogVisible = true;
        },
        clothClose: function () {
            this.clothDialogVisible = false;
            this.addClothName = '';
            this.toClothAdd = true;            //展示添加按钮
            this.openingClothAdd = false;      //关闭添加按钮
        },
        toAddCloth: function () {
            this.openingClothAdd = true;      //关闭添加按钮，展示输入框
            this.toClothAdd = false;
        },
        dominateClothAdd: function () {
            this.cloth.name = '';
            this.cloth.price = '';
            this.cloth.parentName = '';
        },
        addCloth: function () {
            var _this = this;
            _this.cloth.name = _this.addClothName;
            _this.cloth.price = _this.addClothPrice;
            _this.cloth.parentName = _this.addClothParent;
            var data = {
                method: 'post',
                url: context + "/clothingType/",
                data: _this.cloth,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.query13.key = '';
                    _this.listCloth();
                    _this.dominateClothAdd();
                    _this.$message.success("服务类型添加成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("clothadd error");
            })
        },
        saveCloth: function (cloth) {
            var _this = this;
            var data;
            if (cloth.name === '' || cloth.name === undefined) {  // 删除
                data = {
                    method: 'delete',
                    url: context + '/clothingType/' + cloth.id,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listCloth();
                        _this.$message.success("衣物类型删除成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("clothdelete error");
                })
            } else {                                         // 编辑
                // _this.brand.id = brand.id;
                // _this.brand.name = brand.name;
                data = {
                    method: 'put',
                    url: context + '/clothingType',
                    data: cloth,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listCloth();
                        _this.$message.success("衣物类型修改成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console.log("editcloth error");
                })
            }
        },
        listCloth: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/clothingType/list',
                data: _this.query13,
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
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console("clothList error");
            })
        },
        //品牌
        toAddBrand: function () {
            this.toBAdd = false;
            this.openingBAdd = true;
        },
        closeAddBrand: function () {
            this.toBAdd = true;
            this.openingBAdd = false;
        },
        addBrand: function () {
            var _this = this;
            if (_this.addBrandName === '' || _this.addBrandName === undefined) {
                return;
            }
            _this.b.name = _this.addBrandName;
            var data = {
                method: 'post',
                url: context + '/brand',
                data: _this.b,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.query13.key = '';
                    // _this.closeAddBrand();
                    _this.listBrand();
                    _this.$message.success("品牌信息添加成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console("brandList error");
            })
        },
        editBrand: function (brand) {
            var _this = this;
            var data;
            if (brand.name === '' || brand.name === undefined) {  // 删除
                data = {
                    method: 'delete',
                    url: context + '/brand/' + brand.id,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listBrand();
                        _this.$message.success("品牌信息删除成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("branddelete error");
                })
            } else {                                         // 编辑
                // _this.brand.id = brand.id;
                // _this.brand.name = brand.name;
                data = {
                    method: 'put',
                    url: context + '/brand',
                    data: brand,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.query13.key = '';
                        _this.listBrand();
                        _this.$message.success("品牌信息修改成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("editbrand error");
                })
            }
        },
        listBrand: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/brand',
                params: _this.query13,
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
                console("brandList error");
            })
        },
        initQuery() {
            this.query13.key = '';
        },
        showBrand: function () {
            var _this = this;
            _this.listBrand();
            _this.brandDialogVisible = true;
        },
        showColor: function () {
            var _this = this;
            _this.listColor();
            _this.colorDialogVisible = true;
        },
        listColor: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/color',
                params: _this.query13,
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
                console.log("colorList error");
            })
        }
        ,
        toAddColor: function () {
            this.openingCAdd = true;
            this.toCAdd = false;
        },
        closeColorAdd: function () {
            this.openingCAdd = false;
            this.toCAdd = true;
        },
        addColor: function () {
            var _this = this;
            if (_this.addColorName === '' || _this.addColorName === undefined) {
                return;
            }
            _this.color.name = _this.addColorName;
            var data = {
                method: 'post',
                url: context + '/color',
                data: _this.color,
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.closeColorAdd();
                    _this.query13.key = '';
                    _this.listColor();
                    _this.$message.success("颜色信息添加成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("addcolor error");
            })
        },
        saveColor: function (color) {
            var _this = this;
            var data;
            if (color.name === '' || color.name === undefined) {  // 删除
                data = {
                    method: 'delete',
                    url: context + '/color/' + color.id,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listColor();
                        _this.$message.success("颜色信息删除成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("colordelete error");
                })
            } else {                                         // 编辑
                // _this.brand.id = brand.id;
                // _this.brand.name = brand.name;
                data = {
                    method: 'put',
                    url: context + '/color',
                    data: color,
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.listColor();
                        _this.$message.success("颜色信息修改成功");
                    } else {
                        _this.$message.warning(data.msg);
                    }
                }).catch(function (error) {
                    console("editcolor error");
                })
            }
        },
        colorClose: function (done) {
            done();
            this.colorDialogVisible = false;
            this.toCAdd = true;            //展示添加
            this.openingCAdd = false;      //关闭添加
            this.addColorName = '';
            this.initQuery();
        },
        brandClose: function (done) {
            done();
            this.brandDialogVisible = false;
            this.openingBAdd = false;
            this.toBAdd = true;
            this.addBrandName = '';
            this.initQuery();
        },
        //1-3
        //会员查询关闭
        handleClose: function (done) {
            done();
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.addVip = false;
            this.resetFormValue('customerRef');
            this.vipQueryDialogVisible = false;
            this.queryVip.phone = '';
        },
        chanceClose: function () {
            this.resetFormValue('customerRef');
            this.addVip = false;
            this.notVip = true;
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.vipQueryDialogVisible = false;
            this.queryVip.phone = '';
        },
        resetFormValue: function (formName) {
            var _this = this;
            if (_this.$refs[formName] !== null) {
                _this.$nextTick(function () {
                    _this.$refs[formName].resetFields();
                })
            }
        },

        handleSelectCloth(key, keyPath) {
            var _this = this;
            _this.listCloth();
        },
    }
});