var vm = new Vue({
    el: '#orders',
    created() {
        var _this = this;
        _this.list();
    },
    mounted() {

    },
    updated: function () {
        this.$refs.ordersListRef.doLayout();
    },
    data: function () {
        var checkRechargeAmount = (rule, value, callback) => {    // 验证预算
            if (value) {
                if (/^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/.test(value)) {
                    callback();
                } else {
                    callback(new Error('结账金额格式错误'));
                }
            } else {
                callback(new Error('结账金额格式错误'));
            }
        };
        return {
            query: {
                key: '',
                currentPage: 1,
                size: 10,
                order: 'desc',
                orderKey: 'createdAt',
                laundryId: '',
                total: 0,
            },
            ordersList: [],
            myTableHeight: '85%',
            dialogTableVisible: false,
            ordersLog:{
                amount:0,
                des:'',
            },
            submitDialog:false,
            rules:{
                amount:[
                    {required: true, message: '结账金额格式错误', trigger: 'change'},
                    {validator: checkRechargeAmount, message: '结账金额格式错误', trigger: 'blur'}
                    ]
            },
        }
    },
    methods: {
        initDialog:function(){
            this.ordersLog.amount=0;
            this.des='';
        },
        handleSubmit:function(){
            this.submitDialog=true;
            this.getIncomeByDay();
            this.initDialog()
        },
        handleClose: function (done) {
            done();
            this.initDialog()
            this.submitDialog = false;
        },
        handleChance: function () {
            this.initDialog()
            this.submitDialog = false;
        },
        submit:function(){
            var _this=this;
            var data={
                method:'post',
                url:context+'/ordersLog',
                data:_this.ordersLog
            };
            axios(data).then(function(rs){
                var data=rs.data;
                if (data.status===1){
                    _this.$message.success("结账成功");
                    _this.handleChance();
                    _this.list();
                }
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

        getIncomeByDay: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/hangingPoint/getIncomeByDay',
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.ordersLog.amount = data.data;
                } else {
                    _this.$message.warning(data.msg);
                }
            }).catch(function (error) {

            })
        },
        list: function () {
            var _this = this;
            var data = {
                method: 'get',
                url: context + '/ordersLog',
                data: _this.query
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                if (data.status === 1) {
                    _this.query.total = data.data.total;
                    _this.query.currentPage = data.data.current;
                    _this.query.size = data.data.size;
                    if (_this.query.total > 0) {
                        _this.ordersList = data.data.records;
                    } else {
                        _this.ordersList = [];
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