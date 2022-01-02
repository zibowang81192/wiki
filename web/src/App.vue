<template>
  <a-layout id="components-layout-demo-top-side">
    <the-header></the-header>
    <router-view/>
    <a-layout-footer style="text-align: center">
      Wiki ©2022 Created by Zibo Wang
    </a-layout-footer>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import TheHeader from '@/components/the-header.vue';

export default defineComponent({
  name: 'app',
  components:{
    TheHeader
  },
  props: {
    msg: String,
  },
});
</script>

<style>
#components-layout-demo-top-side .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
}
</style>
