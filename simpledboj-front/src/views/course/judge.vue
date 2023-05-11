<template>
  <div style="padding: 20px;">
    <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%">
      <el-table-column label="ID" width="300">
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column prop="moduleName" label="模块名" width="120">
      </el-table-column>
      <el-table-column label="状态" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <div v-if="scope.row.status == -1">失败</div>
          <div v-if="scope.row.status == 0">队列中</div>
          <div v-if="scope.row.status == 1">编译中</div>
          <div v-if="scope.row.status == 2">打包中</div>
          <div v-if="scope.row.status == 3">运行中</div>
          <div v-if="scope.row.status == 4">成功</div>
        </template>
      </el-table-column>
      <el-table-column prop="result" label="结果" width="120">
      </el-table-column>
      <el-table-column prop="errorcase" label="错误案例" width="120">
      </el-table-column>
      <el-table-column prop="costtime" label="耗时(mm)" width="120">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getRecordList } from '@/api/record'
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: []
    }
  },
  created() {
    this.listRecord()
  },
  methods: {
    listRecord() {
      getRecordList().then(data => {
        this.tableData = data.data.list
        console.log(this.tableData)
      })
    }
  }
}
</script>
