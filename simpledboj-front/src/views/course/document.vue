<template>
  <div style="padding: 20px;">
    <div v-html="doc"></div>
  </div>
</template>

<script>
import { getDocument } from "@/api/chapter";
export default {
  data() {
    return {
      doc: ''
    }
  },
  mounted() {
    getDocument(this.$route.query.id).then((data) => {
        this.doc = data.data.doc;
      });
  }
}
</script>

<style>

</style>
