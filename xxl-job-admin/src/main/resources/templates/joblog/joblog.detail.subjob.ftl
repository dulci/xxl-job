<#if (subjobLogs?size gt 0)>
<table class="table table-bordered table-striped display" width="100%">
    <thead>
    <tr>
        <th class="text-center">子任务ID</th>
        <th class="text-center">序号</th>
        <th class="text-center">子任务总数</th>
        <th class="text-center">调度时间</th>
        <th class="text-center">调度地址</th>
        <th class="text-center">调度备注</th>
        <th class="text-center">调度结果</th>
        <th class="text-center">执行时间</th>
        <th class="text-center">执行结果</th>
        <th class="text-center">执行备注</th>
        <th class="text-center">操作</th>
    </tr>
    </thead>
    <tbody>
        <#list subjobLogs as log>
        <tr>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.index}</td>
            <td class="text-center">${log.total}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">${log.id}</td>
            <td class="text-center">11</td>
        </tr>
        </#list>
    </tbody>
</table>
</#if>