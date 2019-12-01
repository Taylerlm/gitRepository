<template>
  <v-card>
    <v-card-title flat color="white">
      <v-btn color="primary" v-on:click="addGoods">新增</v-btn>
      <v-spacer />
      <v-flex xs3>
        状态：
        <v-btn-toggle v-model="filter.saleable">
          <v-btn flat>
            全部
          </v-btn>
          <v-btn flat :value="true">
            上架
          </v-btn>
          <v-btn flat :value="false">
            下架
          </v-btn>
        </v-btn-toggle>
      </v-flex>
      <!--搜索框，与search属性关联-->
      <v-text-field label="输入关键字搜索" v-model.lazy="filter.search"　append-icon="search"　hide-details/>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="goodsList"
      :search="filter.search"
      :pagination.sync="pagination"
      :total-items="totalGoods"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.title }}</td>
        <td class="text-xs-center">{{ props.item.cname }}</td>
        <td class="text-xs-center">{{ props.item.bname }}</td>
        <td class="justify-center layout">
          <v-btn color="info" @click="editGoods(props.item)">编辑</v-btn>
          <v-btn color="warning">删除</v-btn>
          <v-btn >下架</v-btn>
        </td>
      </template>
    </v-data-table>
    <!--弹出对话框-->
    <v-dialog max-width="1000" v-model="show" persistent scrollable>
      <v-card>
        <!--对话框标题-->
        <v-toolbar dense dark color="primary">
          <v-toolbar-title>{{isEdit ? '修改':'新增'}}商品</v-toolbar-title>
          <v-spacer/>
          <!--关闭窗口的按钮-->
          <v-btn icon v-on:click="closeWindow"><v-icon>close</v-icon></v-btn>
        </v-toolbar>
        <!--对话框内容 表单-->
        <v-card-text class="px-5" style="height: 600px;">
          <MyGoodsForm  v-on:close="closeWindow" :oldGoods="oldGoods" :isEdit="isEdit" :step="step"></MyGoodsForm>
        </v-card-text>
        <v-card-actions class="elevation-10">
          <v-flex class="xs3 mx-auto">
            <v-btn @click="previous" color="primary">上一步</v-btn>
            <v-btn @click="next" color="primary">下一步</v-btn>
          </v-flex>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>



<script>
  // 导入自定义的表单组件
  import MyGoodsForm from './MyGoodsForm'
    export default {
        name:"my-goods",
      data(){
        return {
          totalGoods: 0,//总条数
          goodsList: [],//品牌数据
          loading: true,//是否在加载中
          pagination: {},//分页信息
          headers:[
            {text: 'id', align: 'center', value: 'id'},
            {text: '标题', align: 'center', value: 'title', sortable: false},
            {text: '分类名', align: 'center', value: 'cname', sortable: false},
            {text: '品牌名', align: 'center', value: 'bname'},
            {text: '操作', align: 'center', value: 'id', sortable: false }

          ],
          show:false,
          oldGoods:{},
          isEdit:false,
          filter:{
            saleable:'',
            search:''
          },
          step:1

        }

      },

      methods:{
        getDataFromServer(){
          this.$http.get("/item/goods/spu/page",{
            params: {
              page: this.pagination.page, // 当前页
              rows: this.pagination.rowsPerPage, // 每页条数
              sortBy: this.pagination.sortBy, // 排序字段
              desc: this.pagination.descending, // 是否降序
              key: this.filter.search,// 查询字段
              saleable:this.filter.saleable
            }
          }).then(resp=>{
            this.totalGoods = resp.data.total; // 总条数
            this.goodsList = resp.data.currentItems; // 品牌数据
            this.loading = false; // 加载完成
          });
        },
        addGoods:function(){
          this.isEdit = false;
          this.oldGoods = null;
          this.show=true;
        },
        closeWindow:function(){
          this.show=false;
          this.getDataFromServer();
        },
        editGoods : function (oldGoods) {
          this.oldGoods = oldGoods;
          let categories = new Array();
          categories.push(oldGoods.cid1);
          categories.push(oldGoods.cid2);
          categories.push(oldGoods.cid3);
          this.oldGoods.categories = categories;
          //oldGoods中只有spu的基本信息，不包含商品详情，和ｓｋｕ的相关信息
          this.$http.get("/item/goods/spuDetail/"+oldGoods.id).then(resp=>{
              this.oldGoods.spuDetail = resp.data;

          });
          if(this.oldGoods.spuDetail){
            this.$http.get("/item/goods/skus/"+oldGoods.id).then(resp=>{
              this.oldGoods.skus = resp.data;
              this.show=true;
              this.isEdit = true;
              console.info(oldGoods)
            });
          }

        },
        previous:function(){
          if(this.step>1){
            this.step--;
          }
        },
        next:function(){
          if(this.step < 4){
            this.step++;
          }
        }

      },
      // 渲染后执行
      mounted(){
        this.getDataFromServer(); // 调用数据初始化函数
      },
      watch:{
        pagination:{//监视ｐａｇｉｎａｔｉｏｎ属性的变化
          deep:true,//监视pagination中的属性以及属性对象中属性的变化，递归监视
          handler(){
            this.getDataFromServer();
          }
        },
        filter:{
          deep: true,
          handler(){
            this.getDataFromServer();
          }
        }
      },
      components:{
        MyGoodsForm
      }
    }
</script>
<style scoped>
</style>
