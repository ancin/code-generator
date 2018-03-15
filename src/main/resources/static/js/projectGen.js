
var vm = new Vue({
    el:'#projectDiv',
    data:{
        q:{
            tableName: null
        }
    },
    methods: {
        query: function () {
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'tableName': vm.q.tableName},
                page:1
            }).trigger("reloadGrid");
        },
        genProject: function () {
            location.href = "sys/generator/projectGen?tables=test";
        }
    }
});