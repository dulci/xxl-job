<!DOCTYPE html>
<html>
<head>
<#import "../common/common.macro.ftl" as netCommon>
<@netCommon.commonStyle />
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue layout-top-nav">

<div class="wrapper">

    <header class="main-header">
        <nav class="navbar navbar-static-top">
            <div class="container">
            <#-- icon -->
                <div class="navbar-header">
                    <a class="navbar-brand"><b>${I18n.joblog_rolling_log}</b> Console</a>
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#navbar-collapse">
                        <i class="fa fa-bars"></i>
                    </button>
                </div>

            <#-- left nav -->
                <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
                    <ul class="nav navbar-nav">
                    <#--<li class="active" ><a href="javascript:;">任务：<span class="sr-only">(current)</span></a></li>-->
                    </ul>
                </div>

            <#-- right nav -->
                <div class="navbar-custom-menu">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="javascript:window.location.reload();">
                                <i class="fa fa-fw fa-refresh"></i>
                            ${I18n.joblog_rolling_log_refresh}
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
        </nav>
    </header>

    <div class="content-wrapper">
        <section class="content">
            <div style="font-size: 12px; position: relative; background-color: white; display: none;">
                <div id="subJobTable" style="padding: 10px;">
                    <table id="subjob_list" class="table table-bordered table-striped display" width="100%">
                        <thead>
                        <tr>
                            <th class="text-center" name="id">子任务ID</th>
                            <th class="text-center" name="index">序号</th>
                            <th class="text-center" name="total">子任务总数</th>
                            <th class="text-center" name="triggerTime">调度时间</th>
                            <th class="text-center" name="executorAddress">调度地址</th>
                            <th class="text-center" name="triggerCode">调度结果</th>
                            <th class="text-center" name="triggerMsg">调度备注</th>
                            <th class="text-center" name="handleTime">执行时间</th>
                            <th class="text-center" name="handleCode">执行结果</th>
                            <th class="text-center" name="handleMsg">执行备注</th>
                            <th class="text-center" name="finishTime">完成时间</th>
                            <th class="text-center" name="handleMsg">操作</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
            <pre style="font-size: 12px; position: relative;">
                <div id="logConsole"></div>
                <li class="fa fa-refresh fa-spin" style="font-size: 20px;float: left;" id="logConsoleRunning"></li>
                <div><hr><hr></div>
            </pre>
            <div style="font-size: 12px; position: relative;">
                <button class="btn btn-danger btn-xs job_trigger" type="button" id="stop">stop</button>&nbsp;
                <button class="btn btn-primary btn-xs job_trigger" type="button" id="continue">continue</button>
            </div>
        </section>
    </div>

    <!-- footer -->
<@netCommon.commonFooter />

</div>

<@netCommon.commonScript />
<script>
    // 参数
    var triggerCode = '${triggerCode}';
    var handleCode = '${handleCode}';
    var executorAddress = '${executorAddress!}';
    var triggerTime = '${triggerTime?c}';
    var logId = '${logId}';
</script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/js/joblog.detail.1.js"></script>
</body>
</html>