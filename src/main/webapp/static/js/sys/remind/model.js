var vm = new Vue({
    el: '#brandList',
    created() {

    },
    mounted() {
        var _this = this;
        _this.initVipList();
        _this.initOrdersList();
        _this.initOnMsg();
    },
    updated: function () {
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
            queryPick: {
                key: '',
                currentPage: 1,
                size: 5,
                total: 0,
            },
            queryVip: {
                key: '',
                currentPage: 1,
                size: 5,
                total: 0,
            },
            myTableHeight: '85%',
            dialogTableVisible: false,
            vipList: [],
            pickList: [],
            selectVips: [],
            selectPicks: [],
            onMsg: false,
        }
    },
    methods: {
        pickTableRowClassName({row, rowIndex}) {
            if (rowIndex % 2 === 0 && row.status === 0) {
                return 'two-unread-row';
            } else if (rowIndex % 2 === 0 && row.status === 1) {
                return 'two-read-row';
            } else if (rowIndex % 2 === 1 && row.status === 1) {
                return 'one-read-row';
            } else {
                return 'one-unread-row';
            }
        },
        vipTableRowClassName({row, rowIndex}) {
            if (row.remind===true) {
                return 'remind-row';
            } else {
                return 'warning-row';
            }
        },
        sendVips:function(){
            var _this = this;
            if (_this.onMsg===false){
                _this.$message.warning("请开启发送短信功能");
                return
            }
            var phones=''
            for (let i=0;i<_this.selectVips.length;i++){
                if (i===_this.selectVips.length-1){
                    phones+=_this.selectVips[i].phone
                }else {
                    phones+=_this.selectVips[i].phone+','
                }
            }
            var data = {
                method: 'post',
                url: context + '/sms/sendVip',
                params:{
                    phones:phones
                }
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.$message.success("群发短信成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        sendPicks:function(){
            var _this = this;
            if (_this.onMsg===false){
                _this.$message.warning("请开启发送短信功能");
                return
            }
            var phones=''
            for (let i=0;i<_this.selectPicks.length;i++){
                if (i===_this.selectPicks.length-1){
                    phones+=_this.selectPicks[i].phone
                }else {
                    phones+=_this.selectPicks[i].phone+','
                }
            }
            var data = {
                method: 'post',
                url: context + '/sms/sendPick',
                params:{
                    phones:phones
                }
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.$message.success("群发短信成功");
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        handleVipSizeChange: function (value) {
            var _this = this;
            _this.queryVip.size = value;
            _this.initVipList();

        },
        handleVipCurrentChange: function (value) {
            var _this = this;
            _this.queryVip.currentPage = value;
            _this.initVipList();
        },
        handlePickSizeChange: function (value) {
            var _this = this;
            _this.queryPick.size = value;
            _this.initOrdersList();

        },
        handlePickCurrentChange: function (value) {
            var _this = this;
            _this.queryPick.currentPage = value;
            _this.initOrdersList();
        },
        handleChangeOnMsg: function () {
            var _this = this;
            var data = {
                method: 'put',
                url: context + '/dictionary/',
                data:{
                    name:ON_MSG,
                    value:(_this.onMsg===true?'1':'0')
                }
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    if (_this.onMsg===true){
                        _this.$message.success("短信功能已启用");
                    }else {
                        _this.$message.success("短信功能已禁用");
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
                _this.initOnMsg()
            }).catch(function (error) {
            })
        },
        initOnMsg: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/dictionary/' + ON_MSG,
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.onMsg = (data.data === 1);
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },

        handleSelectionVipChange: function (val) {
            this.selectVips = val;

        },
        handleSelectionPickChange: function (val) {
            this.selectPicks = val;
        },

        initVipList: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/customer/checkVipBirth',
                data: _this.queryVip
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.queryVip.total = data.data.total;
                    _this.queryVip.currentPage = data.data.current;
                    _this.queryVip.size = data.data.size;
                    if (_this.queryVip.total > 0) {
                        _this.vipList = data.data.records;
                    } else {
                        _this.vipList = [];
                    }
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {
            })
        },
        initOrdersList: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/orders/listPack',
                data: _this.queryPick
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.queryPick.total = data.data.total;
                    _this.queryPick.currentPage = data.data.current;
                    _this.queryPick.size = data.data.size;
                    if (_this.queryPick.total > 0) {
                        _this.pickList = data.data.records;
                    } else {
                        _this.pickList = [];
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