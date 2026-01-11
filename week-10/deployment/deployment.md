
# 1. 네임스페이스 생성 .

```
kubectl create namespace nginx-deployment
```
![alt text](image.png)



# 2. deployment 생성 

```
kubectl apply -f nginx-deployment.yaml
kubectl get pods
```

![alt text](image-2.png)

# 3. 이미지 버젼 변경


```
kubectl set image deployment/nginx-deployment nginx=nginx:1.21
```

![alt text](image-3.png)
![alt text](image-4.png)
