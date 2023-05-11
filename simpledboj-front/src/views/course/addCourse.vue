<template>
  <div style="padding: 20px">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="margin-top: 20px">
      <el-form-item style="display: flex; flex-direction: row;">
        <el-button type="primary" @click="onSubmit">完成</el-button>
        <el-button @click="clear">清空</el-button>
      </el-form-item>
      <el-form-item label="章节名" prop="moduleName">
        <el-input v-model="form.moduleName" style="width: 240px"></el-input>
      </el-form-item>
      <el-form-item label="父章节">
        <el-cascader :props="{ value: 'id', checkStrictly: true }"
          :options="options" @change="handleChange"></el-cascader>
      </el-form-item>
      <el-form-item label="模块路径" prop="modulePath">
        <el-input v-model="form.modulePath" style="width: 180px"></el-input>
      </el-form-item>
      <el-form-item label="顺序" prop="sort">
        <el-input v-model="form.sort" style="width: 220px" placeholder="可为空，默认为学号后六位"></el-input>
      </el-form-item>
      <el-form-item label="章节简介" prop="details">
        <el-input type="textarea" placeholder="请输入简介" v-model="form.details" :autosize="{ minRows: 5, maxRows: 15 }"
          show-word-limit />
        <!-- <div style="width: 100%; display: flex; flex-direction: row;">
          <tinymce style="width: 45%;" :height="300" v-model="form.details" />
          <el-card style="width: 45%; height: 100%;">
            <div style="display: flex; flex-direction: row; height: 20px">
              <h3 style="position: relative; top: -10px; height: 20px; margin-left: 10px;">章节简介</h3>
            </div>
            <el-divider></el-divider>
            <div style="margin-left: 20px; width: 45%;" v-html="form.details"></div>
          </el-card>
        </div> -->
      </el-form-item>
      <el-form-item label="文档" prop="doc">
        <tinymce style="width: 90%;" :height="500" v-model="form.doc" />
        <el-card style="width: 90%; height: 100%;">
          <div style="display: flex; flex-direction: row; height: 20px">
            <h3 style="position: relative; top: -10px; height: 20px; margin-left: 10px;">文档</h3>
          </div>
          <el-divider></el-divider>
          <div style="margin-left: 20px; width: 45%;" v-html="form.doc"></div>
        </el-card>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addModule, getDocument, chapterTree } from "@/api/chapter";
import Tinymce from '@/components/Tinymce'
export default {
  components: { Tinymce },
  data() {
    return {
      options: [],
      form: {
        moduleName: "",
        parentId: "",
        details: "",
        modulePath: "",
        doc: "",
        sort: 0,
      },
      rules: {
        moduleName: [
          { required: true, message: "请输入章节名", trigger: "blur" },
        ],
        details: [{ required: true, message: "请输入简介", trigger: "blur" }],
        modulePath: [
          { required: true, message: "请输入正确的模块路径", trigger: "blur" },
        ]
      },
    };
  },
  mounted() {
    this.getChapterTree()
    var module = this.$route.query.module
    if (module) {
      this.form.id = module.id
      this.form.details = module.details
      this.form.moduleName = module.label
      this.form.modulePath = module.modulePath
      this.form.sort = module.sort
      getDocument(module.id).then(data => {
        this.form.doc = data.data.doc;
      })
    }

  },
  methods: {
    handleChange(value) {
        this.form.parentId = value[value.length-1]
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
      addModule(this.form).then((data) => {
        this.$message({
          message: data.message,
          type: "success",
        });
      });
    },
    clear() {
      this.form.moduleName = "";
      this.form.modulePath = "";
      this.form.details = "";
      this.form.doc = "";
      this.form.sort = 0;
    },
  },
};
</script>
