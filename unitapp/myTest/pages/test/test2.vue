<template>
	<content>
		<nav-bar :title="title"></nav-bar><!-- 给子组件传值 -->
	</content>
</template>

<script>
	import navbar from '../../components/zpp-nav-bar.vue'
	export default {
		//调用子组件
		components:{
		    'nav-bar':navbar
		},
		onLoad:function(e){
			this.title=e.title
		},
		data() {
			return {
				title:"测试"
			};
		}
	}
</script>

<style>
	@import '../../common/zpp.css';
</style>
