<template>
  <v-stepper v-model="step">
    <v-stepper-header>
      <v-stepper-step :complete="step > 1" step="1">基本信息</v-stepper-step>
      <v-divider/>
      <v-stepper-step :complete="step > 2" step="2">商品描述</v-stepper-step>
      <v-divider/>
      <v-stepper-step :complete="step > 3" step="3">规格参数</v-stepper-step>
      <v-divider/>
      <v-stepper-step step="4">SKU属性</v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>
      <v-stepper-content step="1">
        <v-cascader
          url="/item/category/list"
          required
          showAllLevels
          v-model="goods.categories"
          label="请选择商品分类"/>
        <!--品牌-->
        <v-select
          :items="brandOptions"
          item-text="name"
          item-value="id"
          label="所属品牌"
          v-model="goods.brandId"
          required
          autocomplete
          clearable
          dense chips
        />
        <v-text-field label="商品标题" v-model="goods.title" :counter="200" required />
        <v-text-field label="商品卖点" v-model="goods.subTitle" :counter="200"/>
        <v-text-field label="包装清单" v-model="goods.spuDetail.packingList" :counter="1000" multi-line :rows="3"/>
        <v-text-field label="售后服务" v-model="goods.spuDetail.afterService" :counter="1000" multi-line :rows="3"/>
      </v-stepper-content>
      <v-stepper-content step="2">
        <v-editor v-model="goods.spuDetail.description" upload-url="/upload/image"/>
      </v-stepper-content>
      <v-stepper-content step="3">
        <!--SPU属性-->
        <v-flex class="xs10 mx-auto px-3">
          <!--遍历整个规格参数，获取每一组-->
          <v-card v-for="spec in specifications" :key="spec.group" class="my-2">
            <!--组名称-->
            <v-card-title class="subheading">{{spec.group}}</v-card-title>
            <!--遍历组中的每个属性，并判断是否是全局属性，不是则不显示-->
            <v-card-text v-for="param in spec.params" :key="param.k" v-if="param.global" class="px-5">
              <!--判断是否有可选项，如果没有，则显示文本框。还要判断是否是数值类型，如果是把unit显示到后缀-->
              <v-text-field v-if="param.options.length <= 0"
                            :label="param.k" v-model="param.v" :suffix="param.unit || ''"/>
              <!--否则，显示下拉选项-->
              <v-select v-else :label="param.k" v-model="param.v" :items="param.options"/>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-stepper-content>
      <v-stepper-content step="4">
        <!--SKU属性-->
        <v-flex class="mx-auto">
          <!--遍历特有规格参数-->
          <v-card flat v-for="spec in specialSpecs" :key="spec.k">
            <!--特有参数的标题-->
            <v-card-title class="subheading">{{spec.k}}:</v-card-title>
            <!--特有参数的待选项，需要判断是否有options，如果没有，展示文本框，让用户自己输入-->
            <v-card-text v-if="spec.options.length <= 0" class="px-5">
              <div v-for="i in spec.selected.length+1" :key="i" class="layout row">
                <v-text-field :label="'输入新的' + spec.k" class="flex xs8" v-model="spec.selected[i-1]" v-bind:value="i"/>
                <v-spacer/>
                <v-btn small @click="spec.selected.splice(i-1,i)">删除</v-btn>
              </div>
            </v-card-text>
            <!--如果有options，需要展示成多个checkbox-->
            <v-card-text v-else class="container fluid grid-list-xs">
              <v-layout row wrap class="px-5">
                <v-checkbox color="primary" v-for="o in spec.options" :key="o" class="flex xs3"
                            :label="o" v-model="spec.selected" :value="o"/>
              </v-layout>
            </v-card-text>
          </v-card>
          <v-divider/>
          <!--sku列表-->
          <h3>sku列表：</h3>
          <v-flex>
            <v-card>
            <!--标题-->
            <v-card-title class="subheading">SKU列表</v-card-title>
            <!--SKU表格，hide-actions因此分页等工具条-->
            <v-data-table :items="skus" :headers="headers" hide-actions item-key="indexes">
              <template slot="items" slot-scope="props">
                <tr @click="props.expanded = !props.expanded">
                <!--价格和库存展示为文本框-->
                <td v-for="(v,k) in props.item" :key="k" v-if="['price', 'stock'].includes(k)"
                    class="text-xs-center">
                  <v-text-field single-line v-model.number="props.item[k]"/>
                </td>
                <!--enable展示为checkbox-->
                <td class="text-xs-center" v-else-if="k === 'enable'">
                  <v-checkbox v-model="props.item[k]"/>
                </td>
                <!--indexes和images不展示，其它展示为普通文本-->
                <td class="text-xs-center" v-else-if="!['indexes','images'].includes(k)">{{v}}</td>
                </tr>
              </template>
              <!--点击表格后展开-->
              <template slot="expand" slot-scope="props">
                <v-card flat>
                  <v-layout row>
                    <v-flex>
                      <v-upload
                        v-model="props.item['images']"
                        url="/upload/image"
                        :multiple="true"
                        :pic-width="250"
                        :pic-height="90"
                      />
                    </v-flex>
                  </v-layout>
                </v-card>
              </template>
            </v-data-table>
          </v-card>
          </v-flex>
        </v-flex>
        <!--提交按钮-->
        <v-flex xs3 offset-xs9>
          <v-btn color="info" @click="submit">保存商品信息</v-btn>
        </v-flex>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>

</template>

<style>
    body {
        background-color: #ff0000;
    }
</style>

<script>
    export default {
      name:'MyGoodsForm',
      props:{
        step:{
          type:Number,
          default:1
        },
        oldGoods:{
          type: Object
        },
        isEdit:{
          type:Boolean,
          default: false
        }
      },
      data() {
        return {
          goods:{
            categories:[], // 商品3级分类数组信息
            brandId: 0,// 品牌id信息
            title: '',// 标题
            subTitle: '',// 子标题
            spuDetail: {
              packingList: '',// 包装列表
              afterService: '',// 售后服务
              description:''//商品描述
            }
          },
          brandOptions:[],
          specifications:[],
          specialSpecs:[]
        }
      },
      watch:{
        'goods.categories':{
          deep:true,
          handler(val){
            if(val && val.length >0){
              // 根据分类查询品牌
              // 根据分类加载品牌信息
              this.$http.get("/item/brand/cid/" + val[2].id)
                .then(resp => {
                  this.brandOptions = resp.data;
                });
              this.$http.get("/item/spec/"+val[2].id).then(resp=>{
                this.specifications = resp.data;
                const temp =[];
                resp.data.forEach(({params})=>{
                  params.forEach(({k,options,global})=>{
                    if(!global){
                      temp.push({k,options,selected:[]})
                    }
                  })
                });
                this.specialSpecs = temp;

              })
            }

          }

        },
        oldGoods: {
          handler(val) {
            if(val){
              // 注意不要直接复制，否则这边的修改会影响到父组件的数据，copy属性即可
              this.goods =  Object.deepCopy(val)
              console.log(this.goods);
            }else{
              // 为空，初始化brand
              this.goods = {
                categories:[], // 商品3级分类数组信息
                brandId: 0,// 品牌id信息
                title: '',// 标题
                subTitle: '',// 子标题
                spuDetail: {
                  packingList: '',// 包装列表
                  afterService: '',// 售后服务
                  description:''//商品描述
                }
              }
            }
          },
          deep: true
        }
      },
      computed: {
        skus:function(){
          //过滤掉没有选择的规格参数
          const arr = this.specialSpecs.filter(s=>s.selected.length>0);
          if(!arr || arr.length == 0){
            return [];
          }
          const skusList = arr.reduce((pre,next,index)=>{
            const result = [];
            pre.forEach(p=>{
              let key = next.k;
              for(let i=0;i<next.selected.length;i++){
                const option = next.selected[i];
                let obj = new Object();
                Object.assign(obj,p);
                obj[next.k]=option;
                obj.indexes = (p.indexes||'')+'_'+i;
                if(index == arr.length-1){
                  //最后一组数据
                  Object.assign(obj, { price:0, stock:0,enable:false, images:[]})
                  obj.indexes = obj.indexes.substring(1);
                }
                result.push(obj);

              }
            })
            return result;
          },[{}]);

          return skusList;
        },
        headers(){
          if(this.skus.length<=0){
            return [];
          }
          const headers = [];
          //从ｓｋｕ中任取一个对象，遍历ｋｅｙ
          Object.keys(this.skus[0]).forEach(k=>{
            let value = k;
            if(k === "price"){
              k = "价格"
            }else if(k === "stock"){
              k = "库存"
            }else if(k === 'enable'){
              k = "是否启用"
            }else if(k === "indexes" || k === "images"){
              //不显示　索引和图片
              //ｒｅｔｕｒｎ就是退出当前循环
              return;
            }
            headers.push({
              text:k,
              align:'center',
              sortable:false,
              value
            })
          })
          return headers;
        }
      },
      methods:{
        submit:function(){
          //校验表单
          //将ｇｏｏｄｓ节点中的所有的数据进行重新包装
          const {categories:[{id:cid1},{id:cid2},{id:cid3}],...goodsParams} = this.goods;
          //处理specifications　ｓｐｕ表中不保存ｏｐｔｉｏｎ的选项数据
          //spu 中的specifications　中只保留了全局属性的值，options被去掉了，ｇｌｏｂａｌ为ｆａｌｓｅ的还是保留
          const specs = this.specifications.map(({group,params})=>{
            const newParams = params.map(({options,...rest})=>{
              return rest;
            })
            return {group,params:newParams};
          })
          //specTemplate 是spu中的特有属性的离散值，就是，也就是ｓｋｕ中选中的那些离散值的ｋｅｙ或是ｇｒｏｕｐ对应的离散值
          // 处理特有规格参数模板
          const specTemplate = {};
          this.specialSpecs.forEach(({k, selected}) => {
            specTemplate[k] = selected;
          });
          // 处理sku
          const skuList = this.skus.filter(s => s.enable).map(({price,stock,enable,images,indexes, ...rest}) => {
            // 标题，在spu的title基础上，拼接特有规格属性值
            const title = goodsParams.title + " " + Object.values(rest).join(" ");
            return {
              price: this.$format(price+""),stock,enable,indexes,title,// 基本属性
              images: !images ? '' : images.join(","), // 图片
              ownSpec: JSON.stringify(rest), // 特有规格参数
            }
          });
          Object.assign(goodsParams, {
            cid1,cid2,cid3, // 商品分类
            skuList, // sku列表
          })
          goodsParams.spuDetail.specifications= JSON.stringify(specs);
          goodsParams.spuDetail.specTemplate = JSON.stringify(specTemplate);

          console.log(goodsParams);
          //将数据按照json格式发送，体现是　goodsParams　是json格式的数据
          //也就是这么传contentType　是applicaion/json格式
          this.$http.post("/item/goods/save",goodsParams).then(()=>{
            this.$emit('close');
            this.$message.success("新增成功了");
          }).catch(()=>{
            this.$message.error("保存失败")
          })

        }
      }
    }
</script>
