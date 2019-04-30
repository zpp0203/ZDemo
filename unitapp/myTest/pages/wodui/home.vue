<template>
	<content class="gradient-back">
		<view class="home-top" >
			<view class="home-top-item" v-for="(item, index) in tops" :key="index" 
				@click="topClick(index)" >
				<image class="home-top-item-img" :mode="aspectFit" :src="item.img"></image>
				<view class="home-top-item-text">{{item.name}}</view>
			</view>
		</view>
		
		<swiper class="home-bottom" :indicator-dots="false" :autoplay="false" >
			<swiper-item v-for="(item, index) in bottoms" :key="index">
				<image class="home-bottom-back" src="../../static/wodui/home-bottom.png"></image>
				<view>{{item.name}}</view>
			</swiper-item>
		</swiper>
	</content>
</template>

<script>
	export default {
		computed: {
			
		},
		props:{//接收参数
			
		},
		data() {
			return {
				tops : [
					{
						name:"导出追溯",
						img:'../../static/wodui/home-top1.png',
						back:"#3FCAB7"
					},{
						name:"翻堆时间",
						img:'../../static/wodui/home-top2.png',
						back:"#4CB7D7"
					},{
						name:"超限参数对比",
						img:'../../static/wodui/home-top3.png',
						back:"#3FCAB7"
					},{
						name:"我的渥堆",
						img:'../../static/wodui/home-top3.png',
						back:"#4CB7D7"
					},{
						name:"数据对比",
						img:'../../static/wodui/home-top2.png',
						back:"#3FCAB7"
					},{
						name:"切换",
						img:'../../static/wodui/home-top1.png',
						back:"#4CB7D7"
					}
				],
				bottoms:[
					{
						name:"1"
					},{
						name:"2"
					},{
						name:"3"
					},
				]
				
			};
		},
		methods:{
			topClick(position){
				console.debug("点击："+this.tops[position].name);
				uni.showToast({
					icon:'none',
					
					title:this.tops[position].name
				})
			}
		}
	}
	
</script>

<style>
	@import "../../common/zpp.css";
	
	.home-top{
		display:flex;
		flex-direction: row;
		flex-wrap: wrap;
/* 		margin-top: var(--status-bar-height); */
		padding: 20upx 0upx 20upx 20upx;
		justify-content: center;
	}
	.home-top-item{
		width: 30%;
		height: 166upx;
		background: #3FCAB7;
		margin: 20upx 20upx 0upx 0upx;
		display:flex;
		justify-content: center;
		/* align-items:flex-end; */
	}
	
	.home-top-item-img{
		width: 100%;
		height: 100%;
		/* background-image:url(../../static/wodui/home-top1.png); */
	}
	.home-top-item-text{
 		position: absolute; 
		font-size: 18upx;
		color: #FFFFFF;
		margin-top: 130upx;
	}
	
	.home-bottom{
		width: 100%;
		height: 800upx;
	}
	.home-bottom-back{
		position: absolute;
		height: 700upx;
		width: 100upx;
		left:50%;
		margin-left:-50upx;
		margin-top: 40upx;
	}
</style>
