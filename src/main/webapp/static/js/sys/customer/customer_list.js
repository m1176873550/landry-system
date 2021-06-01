var vm = new Vue({
    el: '#customer',
    created() {
        var _this = this;
        _this.list();
    },
    mounted() {
        var _this = this;
    },
    updated: function () {
        this.$refs.customerListRef.doLayout();
    },
    data: function () {
        var checkPhone = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            var pattern = new RegExp(/^[1]([3-9])[0-9]{9}$/);  // 9位手机号
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('手机号格式错误'));
            }
        };
        var checkPrice = (rule, value, callback) => {    // 验证预算
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
        var checkRechargeAmount = (rule, value, callback) => {    // 验证预算
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
        return {
            query: {
                key: '',
                currentPage: 1,
                size: 10,
                order: 'desc',
                orderKey: 'updatedAt',
                laundryId: '',
                total: 0,
                isVip: 'vip',
            },
            customerList: [],
            myTableHeight: '85%',
            dialogTableVisible: false,
            customer: {
                id: '',
                laundryId: '',
                fullName: '',
                address: '',
                phone: '',
                idCard: '',
                des: '',
                discount: '',
                balance: '',
                nic: '',
                birthday: '',
                updatedAt: '',
                rechargeAmount: '',
            },
            addOrEdit: '',                //新增还是编辑,查看
            addOrEditTitle: '',            //新增还是编辑标题
            customerType: '会员',
            laundryIdOptions: [],       //店铺表
            laundryIdOption: {},
            //时间
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
            },
            //时间
            rules: {
                fullName: [
                    {required: true, message: '职员姓名不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '职员名称格式错误', trigger: 'change'},
                    {validator: checkName, trigger: 'change'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'blur'}
                ],
                des: [
                    {max: 100, message: '职员描述格式错误', trigger: 'change'}
                ],
                balance: [
                    // {required: true, message: '会员余额不能为空', trigger: 'change'},
                    {validator: checkPrice, message: '会员余额格式错误', trigger: 'blur'}
                ],
                discount: [
                    {validator: checkDiscount, message: '折扣格式错误(如0.00-1.00)', trigger: 'change'}
                ],
            },
        }
    },
    methods: {
        //检查按钮权限
        showBtn: function (code) {
            var buttonArray=localStorage.getItem(BUTTON_CODES);
            return buttonArray.match(code);
        },
        handleDropDown: function (command) {
            switch (command) {
                case "1":
                    this.query.isVip = 'customer';
                    this.customerType = '全部';
                    break;
                case "2":
                    this.query.isVip = 'vip';
                    this.customerType = '会员';
                    break;
            }
            this.list();
        },
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
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("generateNic");
            })
        },
        twoDecimalCustomerInfoFormat: function (row, column, cellValue, index) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (reg.test(cellValue)) {
                cellValue = cellValue.toString().replace(reg, '$1.00');
                return cellValue;
            }
        },
        //排序
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "balance") {
                _this.query.orderKey = "balance";
            } else if (column.prop === "discount") {
                _this.query.orderKey = "discount";
            } else if (column.prop === "birthday") {
                _this.query.orderKey = "birthday";
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
        deleteRow: function (id) {
            var _this = this;
            var data = {
                method: 'delete',
                url: context + '/employee/' + id
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
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
                console("del");
            })
        },
        showDel(row) {
            var _this = this;
            _this.$confirm('此操作将永久删除该职员, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                _this.deleteRow(row.id);
            });
        },
        showAdd: function () {
            var _this = this;
            _this.addOrEdit = 'add';
            _this.addOrEditTitle = "新增用户";
            _this.resetFormValue('customerRef');
            //手动重置
            _this.customer.id = '';
            _this.customer.laundryId = '';
            _this.customer.balance = '';
            _this.customer.phone = '';
            _this.customer.address = '';
            _this.customer.des = '';
            _this.customer.fullName = '';
            _this.customer.discount = '';
            _this.customer.birthday = '';
            _this.customer.nic = '';
            _this.dialogTableVisible = true;
        },
        saveEdit: function () {
            var _this = this;
            _this.$refs['customerRef'].validate((valid) => {
                if (valid) {
                    var data;
                    if (_this.addOrEdit === 'edit') {
                        data = {
                            method: 'put',
                            url: context + '/customer',
                            data: _this.customer,
                        };
                    } else if (_this.addOrEdit === 'add') {
                        data = {
                            method: 'post',
                            url: context + '/customer',
                            data: _this.customer,
                        };
                    } else {
                        return false;
                    }
                    axios(data).then(function (rs) {
                        // if (rs.data===403){
                        //     window.parent.location.href="/view/login/login.htm";
                        // }
                        var data = rs.data;
                        if (data.status === 1) {
                            _this.list();
                            _this.chanceClose();
                            _this.$message.success("修改成功");
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {
                        console.log("edit or add Info");
                    })
                }
            });
        },
        resetFormValue: function (formName) {
            var _this = this;
            if (_this.$refs[formName] !== null) {
                _this.$nextTick(function () {
                    _this.$refs[formName].resetFields();
                })
            }
        },
        showEdit: function (row) {
            var _this = this;
            _this.listToCustomer(row);
            _this.addOrEdit = 'edit';
            _this.addOrEditTitle = "编辑顾客";
            _this.dialogTableVisible = true;
        },
        listToCustomer: function (row) {
            this.customer.id = row.id;
            this.customer.fullName = row.fullName;
            this.customer.nic = row.nic;
            this.customer.phone = row.phone;
            this.customer.address = row.address;
            this.customer.des = row.des;
            this.customer.balance = row.balance;
            this.customer.discount = row.discount;
            this.customer.birthday = row.birthday;
            this.customer.laundryId = row.laundryId;
        },
        laundryIdAndName: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/laundry/id_and_name_list',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.laundryIdOptions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console("id_and_name_list  error")
            })
        },
        handleClose: function (done) {
            done();
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.resetFormValue('customerRef');
            this.dialogTableVisible = false;
        },
        chanceClose: function () {
            this.resetFormValue('customerRef');
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.dialogTableVisible = false;
        },
        list: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/customer',
                params: _this.query
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.query.total = data.data.total;
                    _this.query.currentPage = data.data.current;
                    _this.query.size = data.data.size;
                    if (_this.query.total > 0) {
                        _this.customerList = data.data.records;
                    } else {
                        _this.customerList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {

            })
        },
        handleSizeChange: function (value) {
            var _this = this;
            _this.query.size = value;
            _this.list();

        },
        handleCurrentChange: function (value) {
            var _this = this;
            _this.query.currentPage = value;
            _this.list();
        },
    }
});