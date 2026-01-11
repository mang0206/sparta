
# 1. service.yaml 실행결과

```
kubectl get svc 
```

![alt text](image-2.png)


```
kubectl apply -f service.yaml
```


```
minikube service nginx-service -n nginx-deployment --url

```

![alt text](image-1.png)


![alt text](image.png)