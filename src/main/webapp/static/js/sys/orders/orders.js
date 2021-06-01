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
        }
    },
    methods: {
        twoDecimalCustomerInfoFormat: function (row, column, cellValue, index) {
            var reg = /^(\d+)$/;
            //小数不超过两位
            if (reg.test(cellValue)) {
                cellValue = cellValue.toString().replace(reg, '$1.00');
                return cellValue;
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
        isPayFormatter: function (row, column, cellValue, index) {
            if (row.isPay === 1) {
                return "是";
            } else if (row.isPay === 0) {
                return "否";
            } else {
                return;
            }
        },
        orderChange: function (column) {
            let _this = this;
            if (column.prop === "isFinished") {
                _this.query.orderKey = "isFinished";
            } else if (column.prop === "totalPrice") {
                _this.query.orderKey = "totalPrice";
            } else if (column.prop === "isPay") {
                _this.query.orderKey = "isPay";
            } else if (column.prop === "createdAt") {
                _this.query.orderKey = "createdAt";
            }
            if (column.order === DESC) {
                _this.query.order = 'desc'
            } else if (column.order === ASC) {
                _this.query.order = 'asc'
            }
            _this.list();
        },
        list: function () {
            var _this = this;
            var data = {
                method: 'post',
                url: context + '/orders',
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