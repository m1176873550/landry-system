var vm = new Vue({
    el: '#summaryGraph',
    created() {
        var _this = this;
        _this.handleDropDown(0);
        _this.handleChangeIncome(0);
    },
    mounted() {
    },
    updated: function () {
    },
    data: function () {
        return {
            days: 30,
            seriesData: {},
            xAxisData: [],
            //统计时间区间
            daysName: '昨天',
            incomeData: {
                totalIncome: 0,
                one: 0,
                two: 0,
                three: 0,
                four: 0,
                five: 0,
                six: 0,
                seven: 0,
                eight: 0,
                nine: 0,
                ten: 0,
                eleven: 0,
                twelve: 0,
            },
            notUsedMoneyData: {
                totalPrice: 0,
                one: 0,
                two: 0,
                three: 0,
                four: 0,
                five: 0,
                six: 0,
                seven: 0,
                eight: 0,
                nine: 0,
                ten: 0,
                eleven: 0,
                twelve: 0,
            },
            vipMoneyData: {
                totalPrice: 0,
                one: 0,
                two: 0,
                three: 0,
                four: 0,
                five: 0,
                six: 0,
                seven: 0,
                eight: 0,
                nine: 0,
                ten: 0,
                eleven: 0,
                twelve: 0,
            },
            incomeDataTitle: '月订单收入统计',
            incomeDataTitleButton: '最近一年',
            // dateIncomeDataTitle: '日收入统计',
            // dateIncomeDataTitleButton: '日收入统计',
            vipMoneyDataTitle: '会员月收入统计',
            vipMoneyDataTitleButton: '最近一年',
            notUsedMoneyDataTitle: '会员月未使用金额统计',
            notUsedMoneyDataTitleButton: '最近一年',
            radioOrders: '4',
            radioIncome: '3'
        }
    },
    methods: {
        tableRowId: function (index) {
            if (index !== null) {
                return 'main' + index;
            }
        },
        handleChangeIncome: function (command) {
            var _this = this;
            switch (command) {
                case '0':
                    _this.requestIncomeData(12);
                    _this.incomeDataTitleButton = '最近一年';
                    break;
                case '1':
                    _this.requestIncomeData(24);
                    _this.incomeDataTitleButton = '最近两年';
                    break;
                case '2':
                    _this.requestIncomeData(36);
                    _this.incomeDataTitleButton = '最近三年';
                    break;
                case '3':
                    _this.requestIncomeData(0);
                    _this.incomeDataTitleButton = '全部统计';
                    break;
                default:
                    _this.requestIncomeData(0);
                    _this.incomeDataTitleButton = '全部统计';
                    break;
            }
        },
        handleDropDown: function (command) {
            var _this = this;
            switch (command) {
                case '0':
                    _this.requestSeriesData(1);
                    _this.daysName = '昨日';
                    break;
                case '1':
                    _this.requestSeriesData(7);
                    _this.daysName = '最近一周';
                    break;
                case '2':
                    _this.requestSeriesData(30);
                    _this.daysName = '最近一月';
                    break;
                case '3':
                    _this.requestSeriesData(365);
                    _this.daysName = '最近一年';
                    break;
                case '4':
                    _this.requestSeriesData(0);
                    _this.daysName = '全部统计';
                    break;
                default:
                    _this.days = 0;
                    _this.requestSeriesData(0);
                    _this.daysName = '全部统计';
                    break;
            }
        },
        requestSeriesData: function (days) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/orders/get-date-summary?days=" + days,
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
                    _this.seriesData = data.data;
                    _this.drawLine();
                }
            }).catch(function (error) {

            })
        },
        initIncomeData: function () {
            var _this = this;
            _this.incomeData.one = 0;
            _this.incomeData.twelve = 0;
            _this.incomeData.ten = 0;
            _this.incomeData.three = 0;
            _this.incomeData.two = 0;
            _this.incomeData.four = 0;
            _this.incomeData.five = 0;
            _this.incomeData.six = 0;
            _this.incomeData.seven = 0;
            _this.incomeData.eight = 0;
            _this.incomeData.nine = 0;
            _this.incomeData.eleven = 0;
            _this.incomeData.totalIncome = 0;
        },
        requestIncomeData: function (months) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/orders/getDataIncome?months=" + months,
            };
            axios(data).then(function (rs) {
                var data = rs.data;
                // 请求错误
                if (data.status === 0) {
                    _this.$message.error(data.msg);
                } else {    // 请求成功
                    let tempArr = data.data;
                    _this.initIncomeData();
                    if (tempArr.length > 0) {
                        _this.incomeData.totalIncome = tempArr[0].totalPrice;
                        for (let i = 0; i < tempArr.length; i++) {
                            if (tempArr[i].monthTime === 1) {
                                _this.incomeData.one = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 2) {
                                _this.incomeData.two = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 3) {
                                _this.incomeData.three = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 4) {
                                _this.incomeData.four = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 5) {
                                _this.incomeData.five = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 6) {
                                _this.incomeData.six = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 7) {
                                _this.incomeData.seven = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 8) {
                                _this.incomeData.eight = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 9) {
                                _this.incomeData.nine = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 10) {
                                _this.incomeData.ten = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 11) {
                                _this.incomeData.eleven = tempArr[i].income;
                            } else if (tempArr[i].monthTime === 12) {
                                _this.incomeData.twelve = tempArr[i].income;
                            }
                        }
                    }
                }
            }).then(function (rs1) {
                _this.requestNotUserMoneyData(months);
            })
        },
        drawLine: function () {
            // 基于准备好的dom，初始化echarts实例
            let myChart = echarts.init(document.getElementById('dataSummary'));
            // 绘制图表
            myChart.setOption({
                title: {
                    text:'\n 总订单数：' + this.seriesData.totalPrice + '单',
                    textStyle: {
                            fontSize: 15,
                            fontWeight: 'normal',
                            lineHeight: 20,
                        },
                    backgroundColor:'#ccc'
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    top: '20%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['9:00~11:00',
                        '11:00~13:00',
                        '13:00~15:00',
                        '15:00~17:00',
                        '17:00~19:00',
                        '19:00~21:00',
                        '21:00~23:00',]
                },
                yAxis: {
                    type: 'value',
                    // name: "订单数",
                    // nameLocation: 'end',
                    //字体
                    // nameTextStyle: {
                    //     color:'',
                    //     fontStyle:''}
                },
                series: [{
                    name: '订单数',
                    type: 'line',
                    symbolSize: 8,
                    data: [
                        this.seriesData.one,
                        this.seriesData.two,
                        this.seriesData.three,
                        this.seriesData.four,
                        this.seriesData.five,
                        this.seriesData.six,
                        this.seriesData.seven
                    ],
                    // itemStyle: {
                    //     normal: {
                    //         color: 'white',//设置折线折点颜色
                    //         lineStyle: {
                    //             color: 'currentColor'//设置折线线条颜色
                    //         }
                    //     }
                    // },
                }]
            });
        },
        drawIncome: function () {
            // 基于准备好的dom，初始化echarts实例
            let myChart = echarts.init(document.getElementById('incomeSummary'));
            // 绘制图表
            myChart.setOption({
                title: {
                    top:8,
                    text:'总收入：' + (this.incomeData.totalIncome===undefined?0:this.incomeData.totalIncome) + '元 ' +
                        '\n' + this.incomeDataTitle+'：'  + (this.incomeData.totalIncome===undefined?0:this.incomeData.totalIncome) + '元 ' +
                        '\n' + this.notUsedMoneyDataTitle + '总未使用金额：' + (this.notUsedMoneyData.totalPrice===undefined?0:this.notUsedMoneyData.totalPrice) + '元' +
                        '\n' + this.vipMoneyDataTitle + '总充值金额：' + (this.vipMoneyData.totalPrice===undefined?0:this.vipMoneyData.totalPrice) + '元',
                    textStyle: {
                        fontSize: 15,
                        fontWeight: 'normal',
                        lineHeight: 20,
                    }
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    top: '33%',
                    containLabel: true
                },
                legend: {
                    data: ['订单月收入', '会员未使用金额', '会员充值金额'
                    ]
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['1月',
                        '2月',
                        '3月',
                        '4月',
                        '5月',
                        '6月',
                        '7月',
                        '8月',
                        '9月',
                        '10月',
                        '11月',
                        '12月']
                },
                yAxis: {
                    type: 'value',
                    // nameLocation: 'end',
                    //字体
                    // nameTextStyle: {
                    //     color:'',
                    //     fontStyle:''}
                },
                series: [{
                    name: '订单月收入',
                    type: 'line',
                    symbolSize: 8,
                    data: [
                        this.incomeData.one,
                        this.incomeData.two,
                        this.incomeData.three,
                        this.incomeData.four,
                        this.incomeData.five,
                        this.incomeData.six,
                        this.incomeData.seven,
                        this.incomeData.eight,
                        this.incomeData.nine,
                        this.incomeData.ten,
                        this.incomeData.eleven,
                        this.incomeData.twelve,
                    ],
                    // itemStyle: {
                    //     normal: {
                    //         color: 'red',//设置折线折点颜色
                    //         lineStyle: {
                    //             color: '#5787EB'//设置折线线条颜色
                    //         }
                    //     }
                    // },
                }, {
                    name: '会员未使用金额',
                    type: 'line',
                    symbolSize: 8,
                    data: [
                        this.notUsedMoneyData.one,
                        this.notUsedMoneyData.two,
                        this.notUsedMoneyData.three,
                        this.notUsedMoneyData.four,
                        this.notUsedMoneyData.five,
                        this.notUsedMoneyData.six,
                        this.notUsedMoneyData.seven,
                        this.notUsedMoneyData.eight,
                        this.notUsedMoneyData.nine,
                        this.notUsedMoneyData.ten,
                        this.notUsedMoneyData.eleven,
                        this.notUsedMoneyData.twelve,
                    ],
                    // itemStyle: {
                    //     normal: {
                    //         color: 'red',//设置折线折点颜色
                    //         lineStyle: {
                    //             color: '#5787EB'//设置折线线条颜色
                    //         }
                    //     }
                    // },
                }, {
                    name: '会员充值金额',
                    type: 'line',
                    symbolSize: 8,
                    data: [
                        this.vipMoneyData.one,
                        this.vipMoneyData.two,
                        this.vipMoneyData.three,
                        this.vipMoneyData.four,
                        this.vipMoneyData.five,
                        this.vipMoneyData.six,
                        this.vipMoneyData.seven,
                        this.vipMoneyData.eight,
                        this.vipMoneyData.nine,
                        this.vipMoneyData.ten,
                        this.vipMoneyData.eleven,
                        this.vipMoneyData.twelve,
                    ],
                    // itemStyle: {
                    //     normal: {
                    //         color: 'red',//设置折线折点颜色
                    //         lineStyle: {
                    //             color: '#5787EB'//设置折线线条颜色
                    //         }
                    //     }
                    // },
                }]
            });
        },

        handleChangeNotUsedMoney: function (command) {
            var _this = this;
            switch (command) {
                case '0':
                    _this.requestNotUserMoneyData(12);
                    _this.notUsedMoneyDataTitleButton = '最近一年';
                    break;
                case '1':
                    _this.requestNotUserMoneyData(24);
                    _this.notUsedMoneyDataTitleButton = '最近两年';
                    break;
                case '2':
                    _this.requestNotUserMoneyData(36);
                    _this.notUsedMoneyDataTitleButton = '最近三年';
                    break;
                case '3':
                    _this.requestNotUserMoneyData(0);
                    _this.notUsedMoneyDataTitleButton = '全部统计';
                    break;
                default:
                    _this.requestNotUserMoneyData(12);
                    _this.notUsedMoneyDataTitleButton = '最近一年';
                    break;
            }
        },
        requestNotUserMoneyData: function (months) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/customer/getNotUserMoneySummary",
                params: {
                    months: months
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
                    if (data.data !== undefined && data.data !== null) {
                        _this.notUsedMoneyData = data.data;
                    }
                }
            }).then(function (rs1) {
                _this.requestVipMoneyData(months);
            })
        },
        drawNotUsedMoney: function () {
            // 基于准备好的dom，初始化echarts实例
            let myChart = echarts.init(document.getElementById('notUsedMoney'));
            // 绘制图表
            myChart.setOption({
                title: {
                    textStyle: {
                        fontSize: 15,
                        fontWeight: 'normal'
                    },
                    text: this.notUsedMoneyDataTitle + '\n 总未使用金额：' + this.notUsedMoneyData.totalPrice + '元'
                },
                tooltip: {},
                xAxis: {
                    name: '月份',
                    data: ['1',
                        '2',
                        '3',
                        '4',
                        '5',
                        '6',
                        '7',
                        '8',
                        '9',
                        '10',
                        '11',
                        '12']
                },
                yAxis: {
                    name: "金额",
                    nameLocation: 'end',
                    //字体
                    // nameTextStyle: {
                    //     color:'',
                    //     fontStyle:''}
                },
                series: [{
                    name: '会员未使用金额',
                    type: 'line',
                    symbolSize: 8,
                    data: [
                        this.notUsedMoneyData.one,
                        this.notUsedMoneyData.two,
                        this.notUsedMoneyData.three,
                        this.notUsedMoneyData.four,
                        this.notUsedMoneyData.five,
                        this.notUsedMoneyData.six,
                        this.notUsedMoneyData.seven,
                        this.notUsedMoneyData.eight,
                        this.notUsedMoneyData.nine,
                        this.notUsedMoneyData.ten,
                        this.notUsedMoneyData.eleven,
                        this.notUsedMoneyData.twelve,
                    ],
                    itemStyle: {
                        normal: {
                            color: 'red',//设置折线折点颜色
                            lineStyle: {
                                color: '#5787EB'//设置折线线条颜色
                            }
                        }
                    },
                }]
            });
        },
        handleChangeVipSummary: function (command) {
            var _this = this;
            switch (command) {
                case '0':
                    _this.requestVipMoneyData(12);
                    _this.vipMoneyDataTitleButton = '最近一年';
                    break;
                case '1':
                    _this.requestVipMoneyData(24);
                    _this.vipMoneyDataTitleButton = '最近两年';
                    break;
                case '2':
                    _this.requestVipMoneyData(36);
                    _this.vipMoneyDataTitleButton = '最近三年';
                    break;
                case '3':
                    _this.requestVipMoneyData(0);
                    _this.vipMoneyDataTitleButton = '全部统计';
                    break;
                default:
                    _this.requestVipMoneyData(12);
                    _this.vipMoneyDataTitleButton = '最近一年';
                    break;
            }
        },
        requestVipMoneyData: function (months) {
            var _this = this;
            var data = {
                method: 'get',
                url: context + "/customer/vipMoneySum",
                params: {
                    months: months
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
                    if (data.data !== undefined && data.data !== null) {
                        _this.vipMoneyData = data.data;
                    }
                }
            }).then(function (rs2) {
                _this.drawIncome();
            })
        },

        drawVipMoney: function () {
            // 基于准备好的dom，初始化echarts实例
            let myChart = echarts.init(document.getElementById('vipMoneySummary'));
            // 绘制图表
            myChart.setOption({
                title: {
                    textStyle: {
                        fontSize: 15,
                        fontWeight: 'normal'
                    },
                    text: this.vipMoneyDataTitle + ' \n总充值金额：' + this.vipMoneyData.totalPrice + '元'
                },
                tooltip: {},
                xAxis: {
                    name: '月份',
                    data: ['1',
                        '2',
                        '3',
                        '4',
                        '5',
                        '6',
                        '7',
                        '8',
                        '9',
                        '10',
                        '11',
                        '12']
                },
                yAxis: {
                    name: "金额",
                    nameLocation: 'end',
                    //字体
                    // nameTextStyle: {
                    //     color:'',
                    //     fontStyle:''}
                },
                series: [{
                    name: '会员充值金额', type: 'line',
                    symbolSize: 8,
                    data: [
                        this.vipMoneyData.one,
                        this.vipMoneyData.two,
                        this.vipMoneyData.three,
                        this.vipMoneyData.four,
                        this.vipMoneyData.five,
                        this.vipMoneyData.six,
                        this.vipMoneyData.seven,
                        this.vipMoneyData.eight,
                        this.vipMoneyData.nine,
                        this.vipMoneyData.ten,
                        this.vipMoneyData.eleven,
                        this.vipMoneyData.twelve,
                    ],
                    itemStyle: {
                        normal: {
                            color: 'red',//设置折线折点颜色
                            lineStyle: {
                                color: '#5787EB'//设置折线线条颜色
                            }
                        }
                    },
                }]
            });
        },

    }
});