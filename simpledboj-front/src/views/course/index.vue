<template>
  <div style="
      height: calc(100vh - 50px);
      width: 100vw;
      padding: 20px;
      display: flex;
      flex-direction: row;
    ">
    <el-card shadow="always" style="width: 35vw; height: calc(100vh - 100px)">
      <div style="display: flex; flex-direction: row; height: 20px">
        <h3 style="position: relative; top: -10px; height: 20px; margin-left: 10px;">章节列表</h3>
      </div>
      <el-divider></el-divider>
      <el-tree :data="chapterList" node-key="id" @node-click="handleNodeClick" :expand-on-click-node="false" accordion>
        <div class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
        </div>
      </el-tree>
    </el-card>

    <el-card shadow="always" style="width: 45vw; height: calc(100vh - 100px); position: relative; left: 5vw
      display: flex; flex-direction: column;
      ">
      <div style="display: flex; flex-direction: row; height: 20px">
        <h3 style="position: relative; top: -10px; height: 20px; margin-left: 10px;">简介</h3>
        <el-button v-show="curChapterId != ''" @click="getDocument1" type="text"
          style="height: 20px; margin-left: 20px; font-size: 16px">查看文档</el-button>
        <el-button @click="downloadModule1" v-show="curChapterId != ''" type="text"
          style="height: 20px; font-size: 16px">下载代码</el-button>
        <el-button @click="getModuleFile1" v-show="curChapterId != ''" type="text"
          style="height: 20px; font-size: 16px">上传代码</el-button>
      </div>
      <el-divider></el-divider>
      <div class="document-box" v-html="details"></div>
    </el-card>
    <el-drawer title="上传代码" size="50%" :visible.sync="drawer" direction="ltr" :before-close="handleClose">
      <el-table :data="tableData" v-loading="tableLoad" style="width: 100%; margin-left: 20px;">
        <el-table-column prop="filename" label="文件名" width="180">
        </el-table-column>
        <el-table-column prop="details" label="描述" width="280">
        </el-table-column>
        <el-table-column prop="status" label="状态" width="300">
          <template slot-scope="scope">
            <el-progress :text-inside="false" :stroke-width="10"
              :percentage="parseInt(scope.row.status.progress * 100)"></el-progress>
            <el-button @click="openFilePicker(scope.row)" type="text"
              style="height: 20px; font-size: 12px;">上传代码</el-button>
            <input type="file" @change="uploadFile" ref="fileInput" style="display: none" />
          </template>
        </el-table-column>
      </el-table>
      <el-button @click="startJudge1" type="primary"
        style="position: absolute;margin-top: 20px; right: 200px;">开始评测</el-button>
    </el-drawer>
  </div>
</template>

<script>
import { chapterTree, downloadModule, getModuleFile, uploadFileSingle, startJudge } from "@/api/chapter";
import Fakeprogress from 'fake-progress'

export default {
  data() {
    return {
      chapterList: [],
      tableLoad: true,
      curFile: null,
      tableData: [],
      curChapterId: "",
      curChapterLabel: '',
      drawer: false,
      details: `
      <h1 style="text-align: center;">Welcome to SDNU SimpleDB OJ!</h1>
      <h1 style="text-align: center;">选择右侧章节即可查看简介</h1>
      `,
    };
  },
  created() {
    chapterTree().then((data) => {
      this.chapterList = data.data.list;
    });
  },
  methods: {
    judgeDisabled() {
      let flag = false
      this.tableData.forEach(data => {
        let progress = parseInt(data.status.progress * 100)
        if (progress != 100) {
          flag = true
        }
      })
      return flag;
    },
    startJudge1() {
      if (this.judgeDisabled()) {
        this.$message({
          message: "请上传全部文件",
          type: 'info'
        });
        return
      }
      startJudge(this.curChapterId).then(data => {
        this.$message({
          message: data.message,
          type: 'info'
        });
      })
    },
    uploadFile(event) {
      const file = event.target.files[0];
      if (file.name != this.curFile.filename) {
        this.$message({
          message: '请上传正确的文件',
          type: 'warning'
        });
        return
      }
      const formData = new FormData();
      formData.append('file', file);
      formData.append("fileId", this.curFile.id);
      this.curFile.status.start()
      uploadFileSingle(formData).then(data => {
        this.curFile.status.end()
      })
    },
    openFilePicker(data) {
      this.curFile = data
      this.$refs.fileInput.click();
    },
    handleDrawerClick() {
      this.drawer = true
    },
    handleClose(done) {
      this.$confirm('确认关闭？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      })
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    handleNodeClick(data) {
      this.details = data.details;
      this.curChapterId = data.id;
      this.curChapterLabel = data.label
    },
    getDocument1() {
      this.$router.push({
        path: '/course/document',
        query: { id: this.curChapterId }
      })
      // getDocument(this.curChapterId).then((data) => {
      //   this.details = data.data.doc;
      // });
    },
    downloadModule1() {
      downloadModule(this.curChapterId).then((data) => {
        // 后台返回的文件数据
        const fileData = data.data.base64;
        // 创建 Blob 对象
        let binaryString = atob(fileData);
        let blob = new Blob([new Uint8Array(binaryString.length).map((_, i) => binaryString.charCodeAt(i))]);
        const downloadElement = document.createElement('a')
        const href = URL.createObjectURL(blob)
        downloadElement.href = href
        downloadElement.download = `${this.curChapterLabel}.zip`
        document.body.appendChild(downloadElement)
        downloadElement.click()
        document.body.removeChild(downloadElement)
        URL.revokeObjectURL(href)
      });
    },
    getModuleFile1() {
      this.drawer = true
      this.tableLoad = true
      getModuleFile(this.curChapterId).then(data => {
        data.data.list.forEach(item => {
          item.status = new Fakeprogress({
            timeConstant: 600
          })
        })
        this.tableData = data.data.list
        this.tableLoad = false
      })
    }
  },
};
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  /* display: flex; */
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding-right: 8px;
}

.document-box {
  overflow: scroll;
  overflow-x: hidden;
  overflow-y: scroll;
  width: 100%;
  height: calc(100vh - 200px);
}
</style>
