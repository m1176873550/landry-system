var vm = new Vue({
    el: '#role',
    created() {
        var _this = this;
        _this.list();
    },
    mounted() {

    },
    updated: function () {
        this.$refs.roleListRef.doLayout();
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
        return {
            query: {
                key: '',
                currentPage: 1,
                size: 10,
                order: 'desc',
                orderKey: 'updatedAt',
                laundryId: '',
                total: 0,
            },
            defaultProps: {
                children: 'children',
                label: 'name'
            },
            permissions: [],
            roleList: [],
            myTableHeight: '85%',
            dialogTableVisible: false,
            role: {
                id: '',
                permissionIds: [],
                name: '',
                des: '',
                status:'',
            },
            addOrEdit: '',                 //新增还是编辑,查看
            addOrEditTitle: '',            //新增还是编辑标题
            laundryIdOptions: [],          //店铺表
            laundryIdOption: {},
            rules: {
                // laundryId: [
                //     {required: true, message: '所属店铺不能为空', trigger: 'blur'},
                //     // {validator: checkNumAndEnAndCn, trigger: 'change'}
                // ],
                status:[
                    {required: true, message: '权限状态不能为空', trigger: 'blur'},
                ],
                name: [
                    {required: true, message: '角色名称不能为空', trigger: 'blur'},
                    {min: 1, max: 50, message: '角色名称格式错误', trigger: 'change'}
                    // {validator: checkNumAndEnAndCn, trigger: 'change'}
                ],
                des: [
                    {max: 100, message: '角色描述格式错误', trigger: 'change'}
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
        //row状态显示转换
        statusFormatter: function (row, column, cellValue, index) {
            return cellValue === 1 ? '启用' : cellValue === 0 ? '禁用' : '未知';
        },
        //为所选权限赋值
        getCheckedNodes: function () {
            let checkedNodes = this.$refs.tree.getCheckedNodes();
            let ids=[];
            for (let start=0;start<checkedNodes.length;start++){
                ids.push(checkedNodes[start].id)
            }
            this.role.permissionIds=ids;
        },
        //分组权限列表
        permissionsById: function (id) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/roleAndPermission/' + id
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    if (data.data != null && data.data.length > 0) {
                        _this.role.permissionIds = data.data;
                    }else {
                        _this.role.permissionIds = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("groupPermissions");
            })
        },
        //分组权限列表
        groupPermissions: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/permission/group'
            };
            axios(data).then(function (rs) {
                // if (rs.data===403){
                //     window.parent.location.href="/view/login/login.htm";
                // }
                var data = rs.data;
                if (data.status === 1) {
                    _this.permissions = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
                console.log("groupPermissions");
            })
        },
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "updatedAt") {
                _this.query.orderKey = "updatedAt";
            } else {
                _this.$message.warning("排序错误");
            }
            if (column.order === DESC) {
                _this.query.order = 'desc'
            } else if (column.order === ASC) {
                _this.query.order = 'asc'
            } else {
                _this.$message.warning("排序错误");
            }
            _this.list();
        },
        deleteRow: function (id) {
            var _this = this;
            var data = {
                method: 'delete',
                url: context + '/role/' + id
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
        showAdd: function (row) {
            var _this = this;
            _this.addOrEdit = 'add';
            _this.addOrEditTitle = "新增角色";
            _this.resetFormValue('roleRef');
            _this.permissionsById(row.id);
            _this.groupPermissions();
            //手动重置
            _this.role.id = '';
            // _this.customer.laundryId = '';
            _this.role.status = '';
            _this.role.des = '';
            _this.role.name = '';
            _this.dialogTableVisible = true;
        },
        saveEdit: function () {
            var _this = this;
            _this.$refs['roleRef'].validate((valid) => {
                if (valid) {
                    var data;
                    if (_this.addOrEdit === 'edit') {
                        _this.getCheckedNodes();
                        data = {
                            method: 'put',
                            url: context + '/role',
                            data: _this.role,
                        };
                    } else if (_this.addOrEdit === 'add') {
                        data = {
                            method: 'post',
                            url: context + '/role/add',
                            data: _this.role,
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
                        console.log("saveEdit");
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
            _this.listToRole(row);
            _this.permissionsById(row.id);
            _this.groupPermissions();
            _this.addOrEdit = 'edit';
            _this.addOrEditTitle = "编辑角色";
            _this.dialogTableVisible = true;
        },
        listToRole: function (row) {
            this.role.id = row.id;
            this.role.name = row.name;
            this.role.des = row.des;
            this.role.status = row.status;
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
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.resetFormValue('roleRef');
            this.dialogTableVisible = false;
        },
        chanceClose: function () {
            this.resetFormValue('roleRef');
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.dialogTableVisible = false;
        },
        list: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/role',
                data: _this.query
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
                        _this.roleList = data.data.records;
                    } else {
                        _this.roleList = [];
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