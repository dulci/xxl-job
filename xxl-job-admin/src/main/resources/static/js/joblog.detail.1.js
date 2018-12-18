$(function () {

    // trigger fail, end
    if (!(triggerCode == 200 || handleCode != 0)) {
        $('#logConsoleRunning').hide();
        $('#logConsole').append('<span style="color: red;">' + I18n.joblog_rolling_log_triggerfail + '</span>');
        return;
    }

    // init date tables
    var subjobTable = $("#subjob_list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/joblog/subjobTable",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.logId = logId;
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            },
            dataSrc: function (data) {
                if (data.data.length > 0) {
                    $("#subJobTable").parent().show();
                }
                return data.data;
            }
        },
        "searching": false,
        "ordering": false,
        //"scrollX": false,
        "columns": [
            {
                "data": 'id',
                "visible": true,
                "width": '5%'
            },
            {
                "data": 'index',
                "visible": true,
                "width": '5%'
            },
            {
                "data": 'total',
                "visible": true,
                "width": '5%'
            },
            {
                "data": 'triggerTime',
                "width": '10%',
                "render": function (data, type, row) {
                    return data ? moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss") : "";
                }
            },
            {
                "data": 'executorAddress',
                "visible": true,
                "width": '10%'
            },
            {
                "data": 'triggerCode',
                "width": '10%',
                "render": function (data, type, row) {
                    var html = data;
                    if (data == 200) {
                        html = '<span style="color: green">' + I18n.system_success + '</span>';
                    } else if (data == 500) {
                        html = '<span style="color: red">' + I18n.system_fail + '</span>';
                    } else if (data == 600) {
                        html = '<span style="color: red">' + I18n.system_process + '</span>';
                    } else if (data == 0) {
                        html = '';
                    }
                    return html;
                }
            },
            {
                "data": 'triggerMsg',
                "width": '10%',
                "render": function (data, type, row) {
                    return data ? '<a class="logTips" href="javascript:;" >' + I18n.system_show + '<span style="display:none;">' + data + '</span></a>' : I18n.system_empty;
                }
            },
            {
                "data": 'handleTime',
                "width": '10%',
                "render": function (data, type, row) {
                    return data ? moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss") : "";
                }
            },
            {
                "data": 'handleCode',
                "width": '15%',
                "render": function (data, type, row) {
                    var html = data;
                    if (data == 100) {
                        html = '<div style="height: 15px; width: 100%; background-color: goldenrod; border-radius: 7px;"><div style="height: 15px; width: ' + row.persent + '%; background-color: lawngreen; border-radius: 7px;"></div><label style="color: white; position: relative; left: 46%; top: -16px;">' + row.persent + '%</label></div>';
                    } else if (data == 200) {
                        html = '<span style="color: green">' + I18n.joblog_handleCode_200 + '</span>';
                    } else if (data == 500) {
                        html = '<span style="color: red">' + I18n.joblog_handleCode_500 + '</span>';
                    } else if (data == 502) {
                        html = '<span style="color: red">' + I18n.joblog_handleCode_502 + '</span>';
                    } else if (data == 0) {
                        html = '';
                    }
                    return html;
                }
            },
            {
                "data": 'handleMsg',
                "width": '10%',
                "render": function (data, type, row) {
                    return data ? '<a class="logTips" href="javascript:;" >' + I18n.system_show + '<span style="display:none;">' + data + '</span></a>' : I18n.system_empty;
                }
            },
            {
                "data": 'handleMsg',
                "bSortable": false,
                "width": '10%',
                "render": function (data, type, row) {
                    // better support expression or string, not function
                    return function () {
                        if (row.triggerCode == 200 || row.handleCode != 0) {
                            var temp = '<a href="javascript:;" class="logDetail" _id="' + row.id + '">' + I18n.joblog_rolling_log + '</a>';
                            if (row.handleCode == 0) {
                                temp += '<br><a href="javascript:;" class="logKill" _id="' + row.id + '" style="color: red;" >' + I18n.joblog_kill_log + '</a>';
                            }
                            return temp;
                        }
                        return null;
                    }
                }
            }
        ],
        "language": {
            "sProcessing": I18n.dataTable_sProcessing,
            "sLengthMenu": I18n.dataTable_sLengthMenu,
            "sZeroRecords": I18n.dataTable_sZeroRecords,
            "sInfo": I18n.dataTable_sInfo,
            "sInfoEmpty": I18n.dataTable_sInfoEmpty,
            "sInfoFiltered": I18n.dataTable_sInfoFiltered,
            "sInfoPostFix": "",
            "sSearch": I18n.dataTable_sSearch,
            "sUrl": "",
            "sEmptyTable": I18n.dataTable_sEmptyTable,
            "sLoadingRecords": I18n.dataTable_sLoadingRecords,
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": I18n.dataTable_sFirst,
                "sPrevious": I18n.dataTable_sPrevious,
                "sNext": I18n.dataTable_sNext,
                "sLast": I18n.dataTable_sLast
            },
            "oAria": {
                "sSortAscending": I18n.dataTable_sSortAscending,
                "sSortDescending": I18n.dataTable_sSortDescending
            }
        }
    });

    // logTips alert
    $('body').off('click', '.logTips').on('click', '.logTips', function () {
        var msg = $(this).find('span').html();
        ComAlertTec.show(msg);
    });

    // logDetail look
    $('body').off('click', '.logDetail').on('click', '.logDetail', function () {
        var _id = $(this).attr('_id');
        window.open(base_url + '/joblog/logDetailPage?id=' + _id);
        return;
    });

    /**
     * log Kill
     */
    $('body').on('click', '.logKill', function () {
        var _id = $(this).attr('_id');
        layer.confirm((I18n.system_ok + I18n.joblog_kill_log + '?'), {
            icon: 3,
            title: I18n.system_tips,
            btn: [I18n.system_ok, I18n.system_cancel]
        }, function (index) {
            layer.close(index);
            $.ajax({
                type: 'POST',
                url: base_url + '/joblog/logKill',
                data: {"id": _id},
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        layer.open({
                            title: I18n.system_tips,
                            btn: [I18n.system_ok],
                            content: I18n.system_opt_suc,
                            icon: '1',
                            end: function (layero, index) {
                                logTable.fnDraw();
                            }
                        });
                    } else {
                        layer.open({
                            title: I18n.system_tips,
                            btn: [I18n.system_ok],
                            content: (data.msg || I18n.system_opt_fail ),
                            icon: '2'
                        });
                    }
                },
            });
        });

    });

    // Com Alert by Tec theme
    var ComAlertTec = {
        html: function () {
            var html =
                '<div class="modal fade" id="ComAlertTec" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
                '<div class="modal-dialog">' +
                '<div class="modal-content-tec">' +
                '<div class="modal-body"><div class="alert" style="color:#fff;"></div></div>' +
                '<div class="modal-footer">' +
                '<div class="text-center" >' +
                '<button type="button" class="btn btn-info ok" data-dismiss="modal" >' + I18n.system_ok + '</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>';
            return html;
        },
        show: function (msg, callback) {
            // dom init
            if ($('#ComAlertTec').length == 0) {
                $('body').append(ComAlertTec.html());
            }

            // init com alert
            $('#ComAlertTec .alert').html(msg);
            $('#ComAlertTec').modal('show');

            $('#ComAlertTec .ok').click(function () {
                $('#ComAlertTec').modal('hide');
                if (typeof callback == 'function') {
                    callback();
                }
            });
        }
    };
    // pull log
    var fromLineNum = 1;    // [from, to], start as 1
    var pullFailCount = 0;

    function pullLog() {
        // pullFailCount, max=20
        if (pullFailCount++ > 20) {
            logRunStop('<span style="color: red;">' + I18n.joblog_rolling_log_failoften + '</span>');
            return;
        }

        // load
        console.log("pullLog, fromLineNum:" + fromLineNum);

        $.ajax({
            type: 'POST',
            async: true,   // sync, make log ordered
            url: base_url + '/joblog/logDetailCat',
            data: {
                "executorAddress": executorAddress,
                "triggerTime": triggerTime,
                "logId": logId,
                "fromLineNum": fromLineNum
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    if (!data.content) {
                        console.log('pullLog fail');
                        return;
                    }
                    if (fromLineNum != data.content.fromLineNum) {
                        console.log('pullLog fromLineNum not match');
                        return;
                    }
                    if (fromLineNum > data.content.toLineNum) {
                        console.log('pullLog already line-end');

                        // valid end
                        if (data.content.end) {
                            logRunStop('<br><span style="color: green;">[Rolling Log Finish]</span>');
                            return;
                        }

                        return;
                    }

                    // append content
                    fromLineNum = data.content.toLineNum + 1;
                    $('#logConsole').append(data.content.logContent);
                    pullFailCount = 0;

                    // scroll to bottom
                    scrollTo(0, document.body.scrollHeight);        // $('#logConsolePre').scrollTop( document.body.scrollHeight + 300 );

                } else {
                    console.log('pullLog fail:' + data.msg);
                }
            }
        });
    }

    // pull first page
    pullLog();

    // handler already callback, end
    if (handleCode > 0) {
        logRunStop('<br><span style="color: green;">[Load Log Finish]</span><br />');
        return;
    }

    // round until end
    var logRun = setInterval(function () {
        pullLog()
    }, 3000);

    function logRunStop(content) {
        $('#logConsoleRunning').hide();
        logRun = window.clearInterval(logRun);
        $('#logConsole').append(content);
    }
});
