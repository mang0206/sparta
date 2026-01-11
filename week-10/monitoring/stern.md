


PS C:\Users\Admin\IdeaProjects\sparta-msa-project-part-4\week-10\deployment> stern nginx-deployment -n nginx-deployment
+ nginx-deployment-7ccccd94f7-fhj6q › nginx
+ nginx-deployment-7ccccd94f7-hs7p9 › nginx
+ nginx-deployment-7ccccd94f7-xt2mk › nginx
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
nginx-deployment-7ccccd94f7-hs7p9 nginx 10-listen-on-ipv6-by-default.sh: info: Getting the checksum of /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-hs7p9 nginx 10-listen-on-ipv6-by-default.sh: info: Enabled listen on IPv6 in /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Sourcing /docker-entrypoint.d/15-local-resolvers.envsh
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
nginx-deployment-7ccccd94f7-xt2mk nginx 10-listen-on-ipv6-by-default.sh: info: Getting the checksum of /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-fhj6q nginx 10-listen-on-ipv6-by-default.sh: info: Getting the checksum of /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-hs7p9 nginx /docker-entrypoint.sh: Configuration complete; ready for start up
nginx-deployment-7ccccd94f7-xt2mk nginx 10-listen-on-ipv6-by-default.sh: info: Enabled listen on IPv6 in /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-fhj6q nginx 10-listen-on-ipv6-by-default.sh: info: Enabled listen on IPv6 in /etc/nginx/conf.d/default.conf
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: using the "epoll" event method
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Sourcing /docker-entrypoint.d/15-local-resolvers.envsh
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: nginx/1.29.4
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Sourcing /docker-entrypoint.d/15-local-resolvers.envsh
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
nginx-deployment-7ccccd94f7-xt2mk nginx /docker-entrypoint.sh: Configuration complete; ready for start up
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: using the "epoll" event method
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: built by gcc 14.2.0 (Debian 14.2.0-19)
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: OS: Linux 5.15.133.1-microsoft-standard-WSL2
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: nginx/1.29.4
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: built by gcc 14.2.0 (Debian 14.2.0-19)
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: OS: Linux 5.15.133.1-microsoft-standard-WSL2
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: getrlimit(RLIMIT_NOFILE): 1048576:1048576
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker processes
nginx-deployment-7ccccd94f7-fhj6q nginx /docker-entrypoint.sh: Configuration complete; ready for start up
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: getrlimit(RLIMIT_NOFILE): 1048576:1048576
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 29
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: using the "epoll" event method
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: nginx/1.29.4
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 30
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 31
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 32
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker processes
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: built by gcc 14.2.0 (Debian 14.2.0-19)
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 33
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 34
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 35
nginx-deployment-7ccccd94f7-xt2mk nginx 2026/01/11 12:23:41 [notice] 1#1: start worker process 36
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 29
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: OS: Linux 5.15.133.1-microsoft-standard-WSL2
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 30
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: getrlimit(RLIMIT_NOFILE): 1048576:1048576
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 31
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker processes
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 32
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 33
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 29
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 34
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 30
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 35
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 31
nginx-deployment-7ccccd94f7-hs7p9 nginx 2026/01/11 12:23:47 [notice] 1#1: start worker process 36
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 32
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 33
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 34
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 35
nginx-deployment-7ccccd94f7-fhj6q nginx 2026/01/11 12:23:44 [notice] 1#1: start worker process 36