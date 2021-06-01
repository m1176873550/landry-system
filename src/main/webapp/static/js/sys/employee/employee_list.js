var vm = new Vue({
    el: '#employeeList',
    created() {

    },
    mounted() {
        var _this = this;
        _this.list();
        _this.checkIsAdmin();
        _this.laundryIdAndName();
        _this.getRoleIdAndName();
    },
    updated: function () {
        this.$refs.employeeListRef.doLayout();
    },
    data: function () {
        var checkIdCard = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            var pattern = new RegExp(/^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/);  // 身份证号
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('身份证号格式错误'));
            }
        };
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
        var checkName = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            var pattern = new RegExp(/^[A-Za-z0-9\u4e00-\u9fa5]+$/);  // 英文/数字/汉字
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('职员姓名格式错误'));
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

            },
            isAdmin: false,              //是否管理员
            employeeList: [],
            myTableHeight: '85%',
            dialogTableVisible: false,
            employee: {
                id: '',
                laundryId: '',
                loginName: '',
                fullName: '',
                address: '',
                phone: '',
                idCard: '',
                des: '',
                roleId: [],
            },
            roleIdOptions: [],
            roleIdOption: {},
            addOrEdit: '',                //新增还是编辑,查看
            addOrEditTitle: '',            //新增还是编辑标题
            laundryIdOptions: [],       //店铺表
            laundryIdOption: {},
            rules: {
                laundryId: [
                    {required: true, message: '所属店铺不能为空', trigger: 'blur'},
                    // {validator: checkNumAndEnAndCn, trigger: 'change'}
                ],
                fullName: [
                    {required: true, message: '职员姓名不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '职员名称格式错误', trigger: 'change'},
                    {validator: checkName, trigger: 'change'}
                ],
                roleId: [
                    {required: true, message: '职员角色不能为空', trigger: 'blur'},
                ],
                loginName: [
                    {required: true, message: '登陆姓名不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '登陆名称格式错误', trigger: 'change'}
                ],
                idCard: [
                    {required: true, message: '身份证号不能为空', trigger: 'blur'},
                    {validator: checkIdCard, message: '身份证号格式错误', trigger: 'blur'}
                ],
                phone: [
                    {required: true, message: '手机号码不能为空', trigger: 'blur'},
                    {validator: checkPhone, message: '手机号码格式错误', trigger: 'blur'}
                ],
                description: [
                    {max: 100, message: '职员描述格式错误', trigger: 'change'}
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
        //排序
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "updatedAt") {
                _this.query.orderKey = "updatedAt";
            }
            if (column.order === DESC) {
                _this.query.order = 'desc'
            } else if (column.order === ASC) {
                _this.query.order = 'asc'
            }
            _this.list();
        },
        //获取职员对应角色
        getEmployeeRoleId: function (id) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/employeeAndRole/' + id + '/get-role-id'
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.employee.roleId = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("getEmployeeRoleId");
            })
        },
        getRoleIdAndName: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/role/get-role-id-and-name'
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.roleIdOptions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("getRoleIdAndName");
            })
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
                console.log("del");
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
            _this.addOrEditTitle = "新增职员";
            _this.dialogTableVisible = true;
            // _this.resetFormValue('employeeRef');
            //手动重置
            _this.employee.id = '';
            _this.employee.laundryId = '';
            _this.employee.idCard = '';
            _this.employee.phone = '';
            _this.employee.address = '';
            _this.employee.des = '';
            _this.employee.fullName = '';
            _this.employee.loginName = '';
        },
        initEmployee:function(){
            var _this = this;
            _this.employee.id = '';
            _this.employee.laundryId = '';
            _this.employee.idCard = '';
            _this.employee.phone = '';
            _this.employee.address = '';
            _this.employee.des = '';
            _this.employee.fullName = '';
            _this.employee.loginName = '';
        },
        saveEdit: function () {
            var _this = this;
            _this.$refs['employeeRef'].validate((valid) => {
                if (valid) {
                    var data;
                    if (_this.addOrEdit === 'edit') {
                        data = {
                            method: 'put',
                            url: context + '/employee',
                            data: _this.employee,
                        };
                    } else if (_this.addOrEdit === 'add') {
                        data = {
                            method: 'post',
                            url: context + '/employee',
                            data: _this.employee,
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
                            _this.chanceClose();
                            _this.list();
                            _this.$message.success("修改成功");
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {
                        console.log("editInfo");
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
            _this.listToEmployee(row);
            _this.info(row);
            _this.addOrEdit = 'edit';
            _this.addOrEditTitle = "编辑职员";
            _this.dialogTableVisible = true;
        },
        listToEmployee: function (row) {
            this.employee.id = row.id;
            this.employee.fullName = row.fullName;
            this.employee.idCard = row.idCard;
            this.employee.phone = row.phone;
            this.employee.address = row.address;
            this.employee.des = row.des;
            this.employee.loginName = row.loginName;
            this.employee.laundryId = row.laundryId;
        },
        //是否管理员
        checkIsAdmin: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/employee/is-admin',
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1 && data.data === true) {
                    _this.isAdmin = true;
                } else if (data.status === 1 && data.data === false) {
                    _this.isAdmin = false;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("checkIsAdmin  error")
            })
        },
        info: function (row) {
            var _this = this;
            _this.checkIsAdmin();
            _this.getEmployeeRoleId(row.id);
            _this.addOrEditTitle = '查看职员';
            _this.addOrEdit = 'info';
            _this.dialogTableVisible = true;
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
                console.log("laundryIdAndName  error")
            })
        },
        handleClose: function (done) {
            done();
            this.isAdmin = false;
            this.initEmployee();
            this.dialogTableVisible = false;
        },
        chanceClose: function () {
            this.isAdmin = false;
            this.initEmployee();
            this.dialogTableVisible = false;
        },

        list: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/employee',
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
                        _this.employeeList = data.data.records;
                    } else {
                        _this.employeeList = [];
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