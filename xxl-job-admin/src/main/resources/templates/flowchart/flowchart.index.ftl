<!DOCTYPE html>
<html>
<head>
<#import "../common/common.macro.ftl" as netCommon>
<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet"
          href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <!-- daterangepicker -->
    <link rel="stylesheet"
          href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body onload="init()"
      class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
    <!-- header -->
<@netCommon.commonHeader />
    <!-- left -->
<@netCommon.commonLeft "joblog" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>flowchart</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <input type="hidden" value="${jobId}" id="jobId">

            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div id="myDiagramDiv" style="flex-grow: 1; height: 750px; border: solid 1px black" class="box">
                    <#--<div class="box-header hide"><h3 class="box-title">调度日志</h3></div>-->

                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- footer -->
<@netCommon.commonFooter />
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="addNodeModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">选择节点</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form">


                </form>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                    <#--<div class="box-header hide">
                        <h3 class="box-title">调度列表</h3>
                    </div>-->
                        <div class="box-body">
                            <table id="job_list" class="table table-bordered table-striped" width="100%">
                                <thead>
                                <tr>
                                    <th name="id">${I18n.jobinfo_field_id}</th>
                                    <th name="jobSystem">${I18n.jobinfo_field_system}</th>
                                    <th name="jobModule">${I18n.jobinfo_field_module}</th>
                                    <th name="jobDesc">${I18n.jobinfo_field_jobdesc}</th>
                                    <th name="executorParam">${I18n.jobinfo_field_executorparam}</th>
                                    <th name="jobCron">Cron</th>
                                    <th name="mqKey">${I18n.jobinfo_field_mqkey}</th>
                                    <th name="author">${I18n.jobinfo_field_author}</th>
                                    <th name="jobStatus">${I18n.system_status}</th>
                                    <th>${I18n.system_opt}</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                                <tfoot></tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script id="code">
    var myDiagram
    function init() {
        var $ = go.GraphObject.make;  // for conciseness in defining templates
        myDiagram =
                $(go.Diagram, "myDiagramDiv",  // must name or refer to the DIV HTML element
                        {
                            initialContentAlignment: go.Spot.Center,
                            allowDrop: true,  // must be true to accept drops from the Palette
                            "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                            "LinkRelinked": showLinkLabel,
                            scrollsPageOnFocus: false,
                            "undoManager.isEnabled": true  // enable undo & redo
                        });


        // helper definitions for node templates

        function nodeStyle() {
            return [
                // The Node.location comes from the "loc" property of the node data,
                // converted by the Point.parse static method.
                // If the Node.location is changed, it updates the "loc" property of the node data,
                // converting back using the Point.stringify static method.
                new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                {
                    // the Node.location is at the center of each node
                    locationSpot: go.Spot.Center
                }
            ];
        }

        // Define a function for creating a "port" that is normally transparent.
        // The "name" is used as the GraphObject.portId,
        // the "align" is used to determine where to position the port relative to the body of the node,
        // the "spot" is used to control how links connect with the port and whether the port
        // stretches along the side of the node,
        // and the boolean "output" and "input" arguments control whether the user can draw links from or to the port.
        function makePort(name, align, spot, output, input) {
            var horizontal = align.equals(go.Spot.Top) || align.equals(go.Spot.Bottom);
            // the port is basically just a transparent rectangle that stretches along the side of the node,
            // and becomes colored when the mouse passes over it
            return $(go.Shape,
                    {
                        fill: "transparent",  // changed to a color in the mouseEnter event handler
                        strokeWidth: 0,  // no stroke
                        width: horizontal ? NaN : 8,  // if not stretching horizontally, just 8 wide
                        height: !horizontal ? NaN : 8,  // if not stretching vertically, just 8 tall
                        alignment: align,  // align the port on the main Shape
                        stretch: (horizontal ? go.GraphObject.Horizontal : go.GraphObject.Vertical),
                        portId: name,  // declare this object to be a "port"
                        fromSpot: spot,  // declare where links may connect at this port
                        fromLinkable: output,  // declare whether the user may draw links from here
                        toSpot: spot,  // declare where links may connect at this port
                        toLinkable: input,  // declare whether the user may draw links to here
                        cursor: "pointer",  // show a different cursor to indicate potential link point
                        mouseEnter: function (e, port) {  // the PORT argument will be this Shape
                            if (!e.diagram.isReadOnly) port.fill = "rgba(255,0,255,0.5)";
                        },
                        mouseLeave: function (e, port) {
                            port.fill = "transparent";
                        }
                    });
        }

        function textStyle() {
            return {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                stroke: "whitesmoke"
            }
        }

        // define the Node templates for regular nodes

        myDiagram.nodeTemplateMap.add("",  // the default category
                $(go.Node, "Table", nodeStyle(),
                        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
                        $(go.Panel, "Auto",
                                $(go.Shape, "Rectangle",
                                        {fill: "#00A9C9", strokeWidth: 0},
                                        new go.Binding("figure", "figure")),
                                $(go.TextBlock, textStyle(),
                                        {
                                            margin: 8,
                                            maxSize: new go.Size(160, NaN),
                                            wrap: go.TextBlock.WrapFit,
                                            editable: true
                                        },
                                        new go.Binding("text").makeTwoWay())
                        ),
                        // four named ports, one on each side:
                        makePort("T", go.Spot.Top, go.Spot.TopSide, false, true),
                        makePort("L", go.Spot.Left, go.Spot.LeftSide, true, true),
                        makePort("R", go.Spot.Right, go.Spot.RightSide, true, true),
                        makePort("B", go.Spot.Bottom, go.Spot.BottomSide, true, false)
                ));
        // define the Node templates for regular nodes


        // replace the default Link template in the linkTemplateMap
        myDiagram.linkTemplate =
                $(go.Link,  // the whole link panel
                        {
                            routing: go.Link.AvoidsNodes,
                            curve: go.Link.JumpOver,
                            corner: 5, toShortLength: 4,
                            relinkableFrom: true,
                            relinkableTo: true,
                            reshapable: true,
                            resegmentable: true,
                            // mouse-overs subtly highlight links:
                            mouseEnter: function (e, link) {
                                link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
                            },
                            mouseLeave: function (e, link) {
                                link.findObject("HIGHLIGHT").stroke = "transparent";
                            },
                            selectionAdorned: false
                        },
                        new go.Binding("points").makeTwoWay(),
                        $(go.Shape,  // the highlight shape, normally transparent
                                {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
                        $(go.Shape,  // the link path shape
                                {isPanelMain: true, stroke: "gray", strokeWidth: 2},
                                new go.Binding("stroke", "isSelected", function (sel) {
                                    return sel ? "dodgerblue" : "gray";
                                }).ofObject()),
                        $(go.Shape,  // the arrowhead
                                {toArrow: "standard", strokeWidth: 0, fill: "gray"}),
                        $(go.Panel, "Auto",  // the link label, normally not visible
                                {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                                new go.Binding("visible", "visible").makeTwoWay(),
                                $(go.Shape, "RoundedRectangle",  // the label shape
                                        {fill: "#F8F8F8", strokeWidth: 0}),
                                $(go.TextBlock, "Yes",  // the label
                                        {
                                            textAlign: "center",
                                            font: "10pt helvetica, arial, sans-serif",
                                            stroke: "#333333",
                                            editable: true
                                        },
                                        new go.Binding("text").makeTwoWay())
                        )
                );


        // replace the default Link template in the linkTemplateMap
        myDiagram.linkTemplate =
                $(go.Link,  // the whole link panel
                        {
                            routing: go.Link.AvoidsNodes,
                            curve: go.Link.JumpOver,
                            corner: 5, toShortLength: 4,
                            relinkableFrom: true,
                            relinkableTo: true,
                            reshapable: true,
                            resegmentable: true,
                            // mouse-overs subtly highlight links:
                            mouseEnter: function (e, link) {
                                link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
                            },
                            mouseLeave: function (e, link) {
                                link.findObject("HIGHLIGHT").stroke = "transparent";
                            },
                            selectionAdorned: false
                        },
                        new go.Binding("points").makeTwoWay(),
                        $(go.Shape,  // the highlight shape, normally transparent
                                {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
                        $(go.Shape,  // the link path shape
                                {isPanelMain: true, stroke: "gray", strokeWidth: 2},
                                new go.Binding("stroke", "isSelected", function (sel) {
                                    return sel ? "dodgerblue" : "gray";
                                }).ofObject()),
                        $(go.Shape,  // the arrowhead
                                {toArrow: "standard", strokeWidth: 0, fill: "gray"}),
                        $(go.Panel, "Auto",  // the link label, normally not visible
                                {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                                new go.Binding("visible", "visible").makeTwoWay(),
                                $(go.Shape, "RoundedRectangle",  // the label shape
                                        {fill: "#F8F8F8", strokeWidth: 0}),
                                $(go.TextBlock, "Yes",  // the label
                                        {
                                            textAlign: "center",
                                            font: "10pt helvetica, arial, sans-serif",
                                            stroke: "#333333",
                                            editable: true
                                        },
                                        new go.Binding("text").makeTwoWay())
                        )
                );
        myDiagram.addDiagramListener("BackgroundDoubleClicked", function (e) {
//            myDiagram.model.addNodeData(CreateNode());
            showAddNodeModel()
        });
//        myDiagram.addDiagramListener("LinkDrawn", function (e) {
//            console.log(e)
//        });
//        myDiagram.addDiagramListener("SelectionDeleted", function (e) {
//            console.log(e)
//        });

        myDiagram.addDiagramListener("LinkDrawn", function (e) {
            console.log(e.subject.toNode)
            console.log("LinkDrawn")
        });
        myDiagram.addDiagramListener("LinkRelinked", function (e) {
            console.log("LinkRelinked")
        });
        myDiagram.addDiagramListener("LinkReshaped", function (e) {
            console.log("LinkReshaped")
        });
        myDiagram.addDiagramListener("SelectionDeleted", function (e) {
            console.log(e.subject)
            console.log("SelectionDeleted")
        });

        myDiagram.addChangedListener(function (e) {

            // record node insertions and removals
//            if (e.change === go.ChangedEvent.Property || e.change === go.ChangedEvent.Transaction ) {
//                return;
//            }
//            if( e.modelChange ===""){
//                return;
//            }
//            console.log(e.modelChange)
//            console.log(e.propertyName)
            if (e.change === go.ChangedEvent.Insert && e.modelChange === "linkFromKey") {
                console.log(evt.propertyName + " linkFromKey link: " + e.newValue);
            }
        })


        function textStyle() {
            return {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                stroke: "whitesmoke"
            }
        }




        // Make link labels visible if coming out of a "conditional" node.
        // This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
        function showLinkLabel(e) {
            var label = e.subject.findObject("LABEL");
            if (label !== null) label.visible = (e.subject.fromNode.data.category === "Conditional");
        }

        // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
        myDiagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
        myDiagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

        load();  // load an initial diagram from some JSON text

        // initialize the Palette that is on the left side of the page

    } // end init
    var jobTable
    function loadJobTable() {
        if (jobTable) {
            return jobTable;
        }

        jobTable = $("#job_list").dataTable({
            "deferRender": true,
            "processing": true,
            "serverSide": true,
            "ajax": {
                url: base_url + "/flowchart/pageList",
                type: "post",
                data: function (d) {
                    var obj = {};
                    var nodeDataArray = myDiagram.model.nodeDataArray
                    var query = "";
                    var excludeIdList
                    if (nodeDataArray) {
                        for (var i in nodeDataArray) {
                            query = query + "excludeIdList=" + nodeDataArray[i].key + "&"
                        }
                    }
                    query = query + "start=" + d.start + "&"
                    query = query + "length=" + d.length
                    return query;
                }
            },
            "searching": false,
            "ordering": false,
            //"scrollX": true,	// scroll x，close self-adaption
            "columns": [
                {
                    "data": 'id',
                    "bSortable": false,
                    "visible": true,
                    "width": '5%'
                },
                {
                    "data": 'jobSystem',
                    "visible": true,
                    "width": '10%'
                },
                {
                    "data": 'jobModule',
                    "visible": true,
                    "width": '10%'
                },
                {
                    "data": 'jobDesc',
                    "visible": true,
                    "width": '15%'
                },
                {"data": 'executorParam', "visible": false},
                {
                    "data": 'jobCron',
                    "visible": true,
                    "width": '10%'
                },
                {
                    "data": 'mqKey',
                    "visible": true
                },

                {"data": 'author', "visible": true, "width": '10%'},
                {
                    "data": 'jobStatus',
                    "width": '10%',
                    "visible": true,
                    "render": function (data, type, row) {

                        // status
                        if (data && data != 'NONE') {
                            if ('NORMAL' == data) {
                                return '<small class="label label-success" ><i class="fa fa-clock-o"></i>RUNNING</small>';
                            } else {
                                return '<small class="label label-warning" >ERROR(' + data + ')</small>';
                            }
                        } else {
                            return '<small class="label label-default" ><i class="fa fa-clock-o"></i>STOP</small>';
                        }

                        return data;
                    }
                },
                {
                    "data": I18n.system_opt,
                    "width": '15%',
                    "render": function (data, type, row) {
                        return function () {

                            var html = '<p id="' + row.id + '" >' +
                                    '<button class="btn btn-primary btn-xs add-node" type="button">选中</button>  ' +

                                    '</p>';

                            return html;
                        };
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
        return jobTable;

    }


    function showAddNodeModel() {
        loadJobTable().fnDraw();
        $('#addNodeModal').modal({backdrop: false, keyboard: false}).modal('show');
    }
    // job operate
    $("body").on('click', '.add-node', function () {
        var id = $(this).parent('p').attr("id");

        $.ajax({
            type: 'post',
            url: 'flowchart/data',
            data: {
                jobId: id
            },
            dataType: "json",
            success: function (data) {
                myDiagram.model.addNodeDataCollection(data.nodeDataArray);
                myDiagram.model.addLinkDataCollection(data.linkDataArray);
                $('#addNodeModal').modal('hide');
            }
        });

    });
    function load() {

        $.ajax({
            url: 'flowchart/data',
            type: 'post',
            data: {
                jobId: $("#jobId").val()
            },
            dataType: 'json',
            success: function (data) {
                myDiagram.model.nodeDataArray = data.nodeDataArray
                myDiagram.model.linkDataArray = data.linkDataArray

            }
        })

    }


</script>
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/joblog.index.1.js"></script>
</body>
</html>
