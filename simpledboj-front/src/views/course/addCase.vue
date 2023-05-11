<template>
  <div style="padding: 20px;">
    <el-form ref="form" :model="form" :rules="rules" label-width="200px" style="margin-top: 20px;">
      <el-form-item label="章节绑定" prop="moduleId">
        <el-cascader :disabled="selectDisabled" :props="{ value: 'id', checkStrictly: true }" v-model="selectValue"
          :options="options" @change="handleChange"></el-cascader>
      </el-form-item>
      <el-form-item label="上传测试文件" prop="moduleId">
        <el-button :disabled="!form.moduleId" v-if="!form.testFile" @click="openFilePicker()" type="primary">点击上传</el-button>
        <el-progress v-else :stroke-width="8" style="width: 300px; position: relative; top: 15px;"
          :percentage="parseInt(testFileFakeProgress.progress * 100)"></el-progress>
        <input type="file" @change="uploadFile" ref="testFileInput" style="display: none" />
      </el-form-item>
      <el-form-item label="文件名" prop="filename">
        <el-input v-model="form.filename" style="width: 240px;"></el-input>
      </el-form-item>
      <el-form-item label="测试程序文件路径" prop="path">
        <el-input v-model="form.path" style="width: 240px;"></el-input>
      </el-form-item>
      <el-form-item label="测试程序生成文件路径" prop="userFilePath">
        <el-input v-model="form.userFilePath" style="width: 180px;"></el-input>
      </el-form-item>
      <el-form-item label="上传结果文件" prop="moduleId">
        <el-button :disabled="!form.testFile" v-if="!form.resultFile" @click="openFilePicker1()" type="primary">点击上传</el-button>
        <el-progress v-else :stroke-width="8" style="width: 300px; position: relative; top: 15px;"
          :percentage="parseInt(resultFileFakeProgress.progress * 100)"></el-progress>
        <input type="file" @change="uploadFile1" ref="resultFileInput" style="display: none" />
      </el-form-item>
      <el-form-item label="正确结果文件路径" prop="resultPath">
        <el-input v-model="form.resultPath" style="width: 280px;"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">完成</el-button>
        <el-button @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { chapterTree, addCase } from "@/api/chapter";
import Fakeprogress from 'fake-progress'
export default {
  data() {
    return {
      selectDisabled: false,
      selectValue: [],
      options: [],
      testFileFakeProgress: new Fakeprogress({ timeConstant: 600 }),
      resultFileFakeProgress: new Fakeprogress({ timeConstant: 600 }),
      form: {
        filename: '',
        path: '',
        userFilePath: '',
        resultPath: '',
        moduleId: '',
        testFile: '',
        resultFile: ''
      },
      rules: {
        filename: [
          { required: true, message: '不可为空', trigger: 'blur' },
        ],
        path: [
          { required: true, message: '不可为空', trigger: 'blur' },
        ],
        userFilePath: [
          { required: true, message: '不可为空', trigger: 'blur' },
        ],
        resultPath: [
          { required: true, message: '不可为空', trigger: 'blur' },
        ],
        moduleId: [
          { required: true, message: '不可为空', trigger: 'blur' },
        ]
      }
    }
  },
  mounted() {
    this.getChapterTree()
    if (this.$route.query.module) {
      this.form.moduleId = this.$route.query.module.id
      this.selectDisabled = true
      this.selectValue.push(this.$route.query.module.id)
    }
    if (this.$route.query.case) {
      this.form = this.$route.query.case
      // this.form.filename = this.$route.query.case.filename
      // this.form.path = this.$route.query.case.path
      // this.form.userFilePath = this.$route.query.case.userFilePath
      // this.form.resultPath = this.$route.query.case.resultPath
      // this.form.moduleId = this.$route.query.case.moduleId
      // this.form.testFile = this.$route.query.case.testFile
      // this.form.resultFile = this.$route.query.case.resultFile
      this.selectDisabled = true
      this.selectValue.push(this.$route.query.case.moduleId)
    }
  },
  methods: {
    uploadFile1(event) {
      const file = event.target.files[0];
      this.resultFileFakeProgress.start()
      let that = this
      // 创建 FileReader 对象
      const reader = new FileReader();
      // 设置读取文件的回调函数
      reader.onload = function (event) {
        // 读取文件内容
        const fileData = event.target.result;
        // 将文件内容转换为 Base64 编码的字符串
        const encodedData = btoa(fileData);
        // 输出编码后的字符串
        that.form.resultFile = encodedData
        that.resultFileFakeProgress.end()
      };
      // 读取文件内容，并按照 Base64 编码
      reader.readAsBinaryString(file);
    },
    openFilePicker1(data) {
      this.$refs.resultFileInput.click();
    },
    uploadFile(event) {
      const file = event.target.files[0];
      if (!file.name.endsWith('.java')) {
        this.$message({
          message: '请上传正确的文件',
          type: 'warning'
        });
        return
      }
      this.form.filename = file.name
      this.testFileFakeProgress.start()
      let that = this
      // 创建 FileReader 对象
      const reader = new FileReader();
      // 设置读取文件的回调函数
      reader.onload = function (event) {
        // 读取文件内容
        const fileData = event.target.result;
        // 将文件内容转换为 Base64 编码的字符串
        const encodedData = btoa(fileData);
        // 输出编码后的字符串
        that.form.testFile = encodedData
        that.testFileFakeProgress.end()
      };
      // 读取文件内容，并按照 Base64 编码
      reader.readAsBinaryString(file);
    },
    openFilePicker(data) {
      this.$refs.testFileInput.click();
    },
    handleChange(value) {
      this.form.moduleId = value[value.length - 1]
    },
    getChapterTree() {
      chapterTree().then((data) => {
        data.data.list.forEach(elem => {
          if (elem.children.length == 0) {
            elem.children = null
          }
        })
        this.options = data.data.list;
      });
    },
    onSubmit() {
      addCase(this.form).then(data => {
        this.$message({
          message: data.message,
          type: 'success'
        });
      })
    },
    clear() {
      this.form.username = ''
      this.form.name = ''
      this.form.email = ''
      this.form.clazz = ''
      this.form.password = ''
    }
  }
}
</script>
