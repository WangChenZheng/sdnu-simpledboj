<template>
  <div class="app-container">
    <div style="display: flex; flex-direction: row; height: 36px;">
      <span style="line-height: 36px;">点击按钮上传Excel:</span>
      <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" />
      <el-button @click="upload" v-show="showUploadBtn" type="primary" size="medium"
        style="margin-left: 30px;">上传服务器</el-button>
    </div>
    <el-table ref="multipleTable" lazy v-loading="loading" @selection-change="handleSelectionChange" v-show="showUploadBtn" :data="tableData" border
      highlight-current-row style="width: 100%; margin-top:20px;">
      <el-table-column type="selection" width="55" />
      <el-table-column v-for="item of tableHeader" :key="item" :prop="item" :label="item" />
    </el-table>
  </div>
</template>

<script>
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import { addBatchUser } from '@/api/user'

export default {
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableHeader: [],
      showUploadBtn: false,
      loading: true,
      multipleSelection: []
    }
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    beforeUpload(file) {
      return true
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleSuccess({ results, header }) {
      this.tableData = results
      this.tableHeader = header
      this.showUploadBtn = true
      this.loading = false
      setTimeout(() => {
        this.toggleSelection(results)
      }, 1)

    },
    upload() {
      addBatchUser(this.multipleSelection).then(data => {
        this.$message(data.message);
      })
    }
  }
}
</script>
