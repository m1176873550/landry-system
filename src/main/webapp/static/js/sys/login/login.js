var Main = {
    data() {
        var validateName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('账号不能为空'));
            } else {
                var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,20}$|^[A-Za-z]{1,20}$/;
                if (reg.test(value)) {
                    callback();
                } else {
                    callback(new Error('账号格式错误'));
                }
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('密码不能为空'));
            } else {
                var reg = /^[a-zA-Z0-9]{6,20}$/;
                if (reg.test(value)) {
                    callback();
                } else {
                    callback(new Error('密码长度6-20位'));
                }

            }
        };
        return {
            form: {
                name: '',
                password: '',
                rememberStatus: 0
            },
            rememberVO: {
                name: '',
                newPassword:'',
                idCard:''
            },
            sendEmail: '',
            dialogFormVisible: false,
            checked: false,
            rules: {
                name: [
                    {required: true, message: '账号不能为空', trigger: 'blur'},
                    {min: 1, max: 20, message: '账号格式错误', trigger: 'blur'},
                    // {validator: validateName, trigger: 'blur'}
                ],
                idCard:[
                    {required: true, message: '身份证号不能为空', trigger: 'blur'},
                ],
                newPassword: [
                    {validator: validatePass, trigger: 'blur'},
                    {min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur'},
                    {required: true, message: '密码不能为空', trigger: 'blur'}
                ],
            }
        };
    },
    methods: {
        handleClose() {
            var _this = this;
            if (_this.$refs.rememberVO !== null) {
                _this.$nextTick(function () {
                    this.$refs.rememberVO.resetFields();
                    this.dialogFormVisible = false;
                })
            }
        },
        handleFilter: function () {
            this.submitForm();
        },
        rememberClick: function () {
            this.dialogFormVisible = true;
        },
        dialogForm: function () {
            this.$refs.rememberVO.resetFields();
            this.dialogFormVisible = false;
        },

        rememberPassword: function () {
            var _this = this;
            _this.$refs['rememberVO'].validate((valid) => {
                if (valid) {
                    var data = {
                        method: 'put',
                        url: context + '/employee/doForgetPwd',
                        data:_this.rememberVO
                    };
                    axios(data)
                        .then(function (response) {
                            var data = response.data;
                            if (data.status === 1) {
                                _this.$message.success("修改密码成功");
                                _this.$refs['rememberVO'].resetFields();
                                _this.dialogFormVisible = false;
                            } else {
                                _this.$message.error(data.msg);
                            }
                        })
                        .catch(function (error) {
                        });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            })
        },
        submitForm: function () {
            var _this = this;
            _this.$refs['form'].validate((valid) => {
                if (valid) {
                    var data = {
                        method: 'POST',
                        url: context + '/employee/login',
                        data: _this.form
                    };
                    axios(data)
                        .then(function (response) {
                            var data = response.data;
                            if (data.status === 1) {
                                if (data.data === 'employee') {
                                    window.location.href = context + '/view/home/home.htm';
                                    _this.$message.success(data.msg);
                                } else if (data.data === 'customer') {
                                    window.location.href = context + '/view/brand/brand_list.htm';
                                }
                            } else {
                                _this.$message.error(data.msg);
                            }
                        })
                        .catch(function (error) {
                            // window.parent.location.href="/view/login/login.htm";
                        });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },

        change: function () {
            document.getElementById("userName").style.backgroundImage = "url('/static/images/login/IDselected.png')";
            document.getElementById("userNameMaindiv").style.border = "1px solid #1989fa"
        },

        replace: function () {
            document.getElementById("userName").style.backgroundImage = "url('/static/images/login/IDdefault.png')";
            document.getElementById("userNameMaindiv").style.border = "1px solid #a0c4e7"
        },
        change1: function () {
            document.getElementById("password").style.backgroundImage = "url('/static/images/login/passwordselected.png')";
            document.getElementById("passwordMaindiv").style.border = "1px solid #1989fa"
        },

        replace1: function () {
            document.getElementById("password").style.backgroundImage = "url('/static/images/login/passworddefault.png')";
            document.getElementById("passwordMaindiv").style.border = "1px solid #a0c4e7"
        },

    }
};
var Ctor = Vue.extend(Main);
new Ctor().$mount('#app');