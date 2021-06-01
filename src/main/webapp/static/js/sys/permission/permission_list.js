var vm = new Vue({
    el: '#permission',
    mounted: function () {
        var _this = this;
        _this.list();
    },
    updated: function () {
        this.$refs.permissionListRef.doLayout();
    },
    created: function () {
        // this.myTableHeight=document.getElementById("permissionList").clientHeight-140

    },
    data: function () {

        // var checkNumAndEnAndPoint = (rule, value, callback) => {
        //     var pattern = new RegExp(/^[A-Za-z0-9\·\~\！\@\#\￥\%\……\&\*\（\）\——\-\+\=\【\】\{\}\、\|\；\‘\’\：\“\”\《\》\？\，\。\、\`\~\!\#\$\%\^\&\*\(\)\_\[\]{\}\\\|\;\'\'\:\"\"\,\.\/\<\>\?]+$/);    // 数字/英文
        //     if (pattern.test(value)) {
        //         callback();
        //     } else {
        //         callback(new Error('权限按钮编码格式错误'));
        //     }
        // };
        // var checkUrl = (rule, value, callback) => {
        //     var pattern = new RegExp(/^[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]+$/)
        //     if (pattern.test(value)) {
        //         callback();
        //     } else {
        //         callback(new Error('权限菜单页面地址格式错误'));
        //     }
        // };
        var checkdes = (rule, value, callback) => {
            if (value === '') {
                callback();
            }
            var pattern = new RegExp(/^[A-Za-z0-9\u4e00-\u9fa5\·\~\！\@\#\￥\%\……\&\*\（\）\——\-\+\=\【\】\{\}\、\|\；\‘\’\：\“\”\《\》\？\，\。\、\`\~\!\#\$\%\^\&\*\(\)\_\[\]{\}\\\|\;\'\'\:\"\"\,\.\/\<\>\?]+$/);    // 数字/英文/汉字/标点
            if (pattern.test(value)) {
                callback();
            } else {
                callback(new Error('权限描述格式错误'));
            }
        };
        // var checkEnCode = (rule, value, callback) => {
        //     var pattern = /^[0-9]*$/;
        //     if (!value) {
        //         return callback();
        //     }
        //     if (! pattern.test(value)) {
        //         return callback(new Error('编码格式错误'));
        //     }
        //     setTimeout(() => {
        //         var _this = this;
        //         var data = {
        //             method: 'get',
        //             url: context + "/permissions/check_encode",
        //             params: {
        //                 excludid: _this.permissionForm.id,
        //                 encode: _this.permissionForm.enCode
        //             }
        //         };
        //         axios(data).then(function (rs) {
        //             if (rs.data===403){
        //                 window.parent.location.href="/view/login/login.htm";
        //             }
        //             var data = rs.data;
        //             if (data.status === 0) {
        //                 callback(new Error("编码已存在"));
        //             } else if (data.status === 1) {
        //                 callback();
        //             }
        //         }).catch(function (error) {
        //
        //     });
        //     }, 500)
        // }
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
            permissionList: [],
            query: {
                key: '',
                order: '',
                orderKey: '',
                currentPage: '1',
                size: '10',
                total: 0,
            },
            permission: {
                id: '',
                name: '',
                status: '',
                url: '',
                des: '',
                level: '',
                type: '',
                code: '',
                parentId: '',
            },
            editOrAdd: '',           //编辑还是新增
            title: '',               //标题
            dialogTableVisible: false,
            parentIdOptions: [],      //父级对象链表
            formLabelWidth: '10%',
            rules: {
                name: [
                    {required: true, message: '权限名称不能为空', trigger: 'blur'},
                    {min: 1, max: 20, message: '权限名称格式错误', trigger: 'change'},
                    {validator: checkName, message: '权限名称格式错误', trigger: 'change'}
                ],
                level: [
                    {required: true, message: '权限排序不能为空', trigger: 'blur'},
                    // {min: 0, max: 3, message: '权限排序格式错误', trigger: 'blur'},
                ],
                des: [
                    {min: 1, max: 20, message: '权限描述格式错误', trigger: 'change'},
                    {validator: checkdes, message: '权限描述格式错误', trigger: 'change'}
                ],
                type:[
                    {required: true, message: '权限类型不能为空', trigger: 'blur'},
                ],
                status:[
                    {required: true, message: '权限状态不能为空', trigger: 'blur'},
                ],
                parentId:[
                    {required: true, message: '上级权限不能为空', trigger: 'blur'},
                ],
                code:[
                    {required: true, message: '权限编码不能为空', trigger: 'blur'},
                ],
                url:[
                    {required: true, message: '路径不能为空', trigger: 'blur'},
                ],
            }
        }
    },
    methods: {
        //检查按钮权限
        showBtn: function (code) {
            var buttonArray = localStorage.getItem(BUTTON_CODES);
            return buttonArray.match(code);
        },
        getParent: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + "/permission/parent-id-name",
                data:{
                    id:_this.permission.id,
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
                    _this.parentIdOptions=data.data;
                }
            }).catch(function (error) {

            })
        },
        //点击分级触发事件
        clickTable: function (row, column, cell, event) {
            if (row.children && (column.id === 'el-table_1_column_1')) {
                this.$refs.permissionListRef.toggleRowExpansion(row);
            }
        },
        //排序
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "level") {
                _this.query.orderKey = "level";
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
        statusFormatter: function (row, column, cellValue, index) {
            return cellValue === 1 ? '启用' : cellValue === 0 ? '禁用' : '未知';
        },
        resetList: function () {
            this.query.key = '';
            this.list();
        },
        rowTOPermission: function (row) {
            this.permission.id = row.id;
            this.permission.status = row.status;
            this.permission.level = row.level;
            this.permission.parentId = row.parentId;
            this.permission.name = row.name;
            this.permission.des = row.des;
            this.permission.url = row.url;
            this.permission.type = row.type;
            this.permission.code = row.code;

        },
        resetPermission: function () {
            this.permission.id = '';
            this.permission.status = '';
            this.permission.level = '';
            this.permission.parentId = '';
            this.permission.name = '';
            this.permission.des = '';
            this.permission.url = '';
            this.permission.code = '';
            this.permission.type = '';
        },
        showEdit: function (index, row) {
            this.editOrAdd = 'edit';
            this.title='编辑权限';
            this.rowTOPermission(row);
            this.getParent();
            this.dialogTableVisible = true;
        },
        showAdd: function () {
            this.editOrAdd = 'add';
            this.title='新增权限';
            // this.resetFormValue('permissionRef');
            this.resetPermission();
            this.getParent();
            this.dialogTableVisible = true;
        },
        addOrEdit: function () {
            var _this = this;
            _this.$refs.permissionRef.validate((valid) => {
                if (valid) {
                    var data;
                    if (_this.editOrAdd === 'edit') {
                        data = {
                            method: 'put',
                            url: context + '/permission',
                            data: _this.permission,
                        };
                    } else if (_this.editOrAdd === 'add') {
                        data = {
                            method: 'post',
                            url: context + '/permission/add',
                            data: _this.permission,
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
                            _this.$message.success("修改成功");
                            _this.list();
                        } else {
                            _this.$message.warning(data.msg);
                        }
                    }).catch(function (error) {
                        console("editInfo");
                    })
                }
            });
        },
        handleClose: function (done) {
            done();
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.resetFormValue('permissionRef');
            this.dialogTableVisible = false;
        },
        chanceClose: function () {
            this.resetFormValue('permissionRef');
            // this.laundryIdOptions = [];
            // this.laundryIdOption = {};
            this.dialogTableVisible = false;
        },
        resetFormValue: function (formName) {
            var _this = this;
            if (_this.$refs[formName] !== null) {
                _this.$nextTick(function () {
                    _this.$refs[formName].resetFields();
                })
            }
        },
        changeStatus:function(index,row){
            var _this = this;
            var data = {
                method: 'put',
                url: context + "/permission/change-status",
                data: {
                    id:row.id,
                    status:row.status,
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
                    _this.list();
                    _this.$message.success("权限状态修改成功");
                }
            }).catch(function (error) {

            })
        },
        list: function () {   // 查询权限列表
            var _this = this;
            var data = {
                method: 'post',
                url: context + "/permission",
                data: _this.query
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
                    _this.query.total = data.data.total;
                    _this.permissionList = data.data.records;
                }
            }).catch(function (error) {

            })
        },
        del: function (index, row) {
            var _this = this;
            _this.visible = false;
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var data = {
                    method: 'delete',
                    url: context + "/permission/" + row.id
                };
                axios(data).then(function (rs) {
                    // if (rs.data===403){
                    //     window.parent.location.href="/view/login/login.htm";
                    // }
                    var data = rs.data;
                    if (data.status === 1) {
                        _this.$message({
                            message: '删除成功',
                            type: 'success',
                        });
                        _this.list();
                    } else {
                        _this.$message.error(data.msg);
                    }
                }).catch(function (error) {

                });
            }).catch((e) => {
            });
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
